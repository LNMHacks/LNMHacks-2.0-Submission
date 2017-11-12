import time
import requests

name = "parking 1"


def updateStatus(image, goingOut, urlPart):
    # body = {image, name, goingOut}
    files = {'file': open(image, 'rb')}
    response = requests.post('http://192.168.43.175:4000/statusPay/' + urlPart, data={
        'image': image,
        'goingOut': goingOut,
        'name': name
    })

    print(response.content)
    return response.content


serverResp = updateStatus('vehicle_num.jpeg', 0, 'vehiclePicUpload')