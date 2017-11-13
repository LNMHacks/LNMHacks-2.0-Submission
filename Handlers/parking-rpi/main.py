from button import *
import RPi.GPIO as GPIO
import time
import requests

T1_P = 3  # transmitter 1
R1_P = 5  # receiver 1
A_P = 7  # obstacle sensor
BUTTON_P = 8  # button
name = "nehru place"
isRunning = True


def onButtonEvent(button, event):
    if event == BUTTON_PRESSED:
        print "pressed"
        isRunning = False
    elif event == BUTTON_RELEASED:
        print "released"
    elif event == BUTTON_LONGPRESSED:
        print "long pressed"


def setup():
    GPIO.cleanup()
    GPIO.setmode(GPIO.BOARD)
    GPIO.setup(T1_P, GPIO.OUT)
    GPIO.setup(R1_P, GPIO.IN, pull_up_down=GPIO.PUD_UP)
    GPIO.setup(A_P, GPIO.IN, pull_up_down=GPIO.PUD_UP)
    button = Button(BUTTON_P)
    button.addButtonListener(onButtonEvent)
    # GPIO.setup(P_LED, GPIO.OUT)

    # turning IR transmitter and LED on
    GPIO.output(T1_P, GPIO.HIGH)
    # GPIO.output(P_LED, GPIO.HIGH)


def waitToMove(dev1, val1, dev2, val2):
    print "please move ahead"
    while (GPIO.input(dev1) == val1) and (GPIO.input(dev2) == val2):
        time.sleep(0.01)


setup()


def updateStatus(image, goingOut, urlPart):
    # body = {image, name, goingOut}
    files = {'file': open(image, 'rb')}
    response = requests.post('http://localhost:4000/statusPay/' + urlPart, data={
        'image': image,
        'goingOut': goingOut,
        'name': name
    })

    print(response.content)
    return response.content


while isRunning:
    # print GPIO.input(R1_P)
    print GPIO.input(A_P)
    if GPIO.input(R1_P) == GPIO.LOW:  # vehicle may move inside parking
        print 'sensor 1 activated first, vehicle should move inside parking'
        waitToMove(R1_P, GPIO.LOW, A_P, GPIO.HIGH)

        if (GPIO.input(R1_P) == GPIO.HIGH) and (GPIO.input(A_P) == GPIO.HIGH):
            print "vehicle reversed"
            continue
        print 'vehicle moiving inside parking'

        # update server for car number
        serverResp = updateStatus('vehicle_num.jpeg', 0, 'numPicUpload')
        print "vehicle added"

        # update server for availability status
        serverResp = updateStatus('vehicle.jpeg', 0, 'vehiclePicUpload')
        print "availability status updated"


    elif GPIO.input(A_P) == GPIO.LOW:  # vehicle may move outside parking
        print 'sensor 2 activated first, vehicle should move outside parking'
        waitToMove(A_P, GPIO.LOW, R1_P, GPIO.HIGH)

        if (GPIO.input(R1_P) == GPIO.HIGH) and (GPIO.input(A_P) == GPIO.HIGH):
            print "vehicle reversed"
            continue

        print 'vehicle moiving outside parking'

        # update server for car number
        serverResp = updateStatus('vehicle_num.jpeg', 1, 'numPicUpload')
        print "payment done"

        # update server for availability status
        serverResp = updateStatus('vehicle.jpeg', 1, 'vehiclePicUpload')
        print "availability status updated"

    time.sleep(0.01)

GPIO.cleanup()
