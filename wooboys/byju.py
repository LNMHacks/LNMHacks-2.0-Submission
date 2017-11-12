import webbrowser
import cv2
import numpy as np

import os
from subprocess import Popen

byju_min = np.array([140, 84, 73])
byju_max = np.array([155, 255, 154])

hsv_supremum = np.array([220, 221, 255])
hsv_infinum = np.array([150, 100, 130])


from collections import deque

pts = deque()
pts1 = deque()
cap = cv2.VideoCapture(0)

count= np.matrix([[100, 100], [100, 200], [100, 300], [100, 400], [300, 50], [300, 150], [300, 250], [300, 350]])
fill = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

def make_circle1(frame, count=13):
	frame_copy = frame.copy()

	alpha = 0.9

	fill[count] = -1

	cv2.circle(frame_copy,(100,100), 40, (96,96,96), fill[0])
	cv2.line(frame_copy, (100,100-10), (100, 100+10), (0,0,0), 2) 
	cv2.line(frame_copy, (100-10,100), (100+10, 100), (0,0,0), 2)

	cv2.circle(frame_copy,(100,200), 40, (96,96,96), fill[1])
	cv2.line(frame_copy, (100,200-10), (100, 200+10), (0,0,0), 2) 
	cv2.line(frame_copy, (100-10,200), (100+10, 200), (0,0,0), 2)

	cv2.circle(frame_copy,(100,300), 40, (96,96,96), fill[2])
	cv2.line(frame_copy, (100,300-10), (100, 300+10), (0,0,0), 2) 
	cv2.line(frame_copy, (100-10,300), (100+10, 300), (0,0,0), 2)

	cv2.circle(frame_copy,(100,400), 40, (96,96,96), fill[3])
	cv2.line(frame_copy, (100,400-10), (100, 400+10), (0,0,0), 2) 
	cv2.line(frame_copy, (100-10,400), (100+10, 400), (0,0,0), 2)

	cv2.circle(frame_copy,(300,50), 40, (96,96,96), fill[4])
	cv2.line(frame_copy, (300,50-10), (300, 50+10), (0,0,0), 2) 
	cv2.line(frame_copy, (300-10,50), (300+10, 50), (0,0,0), 2)

	cv2.circle(frame_copy,(300,150), 40, (96,96,96), fill[5])
	cv2.line(frame_copy, (300,150-10), (300, 150+10), (0,0,0), 2) 
	cv2.line(frame_copy, (300-10,150), (300+10, 150), (0,0,0), 2)

	cv2.circle(frame_copy,(300,250), 40, (96,96,96), fill[6])
	cv2.line(frame_copy, (300,250-10), (300, 250+10), (0,0,0), 2) 
	cv2.line(frame_copy, (300-10,250), (300+10, 250), (0,0,0), 2)

	cv2.circle(frame_copy,(300,350), 40, (96,96,96), fill[7])
	cv2.line(frame_copy, (300,350-10), (300, 350+10), (0,0,0), 2) 
	cv2.line(frame_copy, (300-10,350), (300+10, 350), (0,0,0), 2)

	cv2.circle(frame_copy,(500,100), 40, (96,96,96), fill[8])
	cv2.line(frame_copy, (500,100-10), (500, 100+10), (0,0,0), 2) 
	cv2.line(frame_copy, (500-10,100), (500+10, 100), (0,0,0), 2)

	cv2.circle(frame_copy,(500,200), 40, (96,96,96), fill[9])
	cv2.line(frame_copy, (500,200-10), (500, 200+10), (0,0,0), 2) 
	cv2.line(frame_copy, (500-10,200), (500+10, 200), (0,0,0), 2)

	cv2.circle(frame_copy,(500,300), 40, (96,96,96), fill[10])
	cv2.line(frame_copy, (500,300-10), (500, 300+10), (0,0,0), 2) 
	cv2.line(frame_copy, (500-10,300), (500+10, 300), (0,0,0), 2)

	cv2.circle(frame_copy,(500,400), 40, (96,96,96), fill[11])
	cv2.line(frame_copy, (500,400-10), (500, 400+10), (0,0,0), 2) 
	cv2.line(frame_copy, (500-10,400), (500+10, 400), (0,0,0), 2)



	frame_ret = cv2.addWeighted(frame, 1-alpha, frame_copy, alpha, 0)


	cv2.imshow('final', frame_ret)

def radcal(der_cenetre):
	rad_cal0 = ((der_cenetre[0]-100)**2 + (der_cenetre[1]-100)**2)**0.5
	rad_cal1 = ((der_cenetre[0]-100)**2 + (der_cenetre[1]-200)**2)**0.5

	rad_cal2 = ((der_cenetre[0]-100)**2 + (der_cenetre[1]-300)**2)**0.5
	rad_cal3 = ((der_cenetre[0]-100)**2 + (der_cenetre[1]-400)**2)**0.5

	rad_cal4 = ((der_cenetre[0]-300)**2 + (der_cenetre[1]-50)**2)**0.5
	rad_cal5 = ((der_cenetre[0]-300)**2 + (der_cenetre[1]-150)**2)**0.5

	rad_cal6 = ((der_cenetre[0]-300)**2 + (der_cenetre[1]-250)**2)**0.5
	rad_cal7 = ((der_cenetre[0]-300)**2 + (der_cenetre[1]-350)**2)**0.5

	rad_cal8 = ((der_cenetre[0]-500)**2 + (der_cenetre[1]-100)**2)**0.5
	rad_cal9 = ((der_cenetre[0]-500)**2 + (der_cenetre[1]-200)**2)**0.5

	rad_cal10 = ((der_cenetre[0]-500)**2 + (der_cenetre[1]-300)**2)**0.5
	rad_cal11= ((der_cenetre[0]-500)**2 + (der_cenetre[1]-400)**2)**0.5


	if rad_cal0 <= 30:
		print('Hola, WE found')
		url = "https://www.facebook.com/"
		webbrowser.open(url, new=2)
		return 0
	elif rad_cal1 <=30:

			# play song
		url = "https://www.youtube.com/watch?v=Bl7t4bK4hPA"
		webbrowser.open(url,new=2)

		return 1
	elif rad_cal2 <=30:
		url = "https://mail.google.com/mail/u/0/#inbox"
		webbrowser.open(url, new=2)
		return 2
	elif rad_cal3 <=30:
		url = "https://docs.google.com/document/u/0/"
		webbrowser.open(url, new=2)
		return 3
	elif rad_cal4 <=30:
		url = "https://drive.google.com/drive/u/0/my-drive"
		webbrowser.open(url, new=2)
		return 4
	elif rad_cal5 <=30:
		# Here a new python file will be opened
		# os.system('python grx.py')
		# call ([" python", " grx.py"])
		subprocess.Popen('grx.py 1', shell='True')
		return 5
	elif rad_cal6 <=30:
		url = "https://www.youtube.com/"
		webbrowser.open(url, new = 2)
		return 6
		# call ([" python", " grx.py"])
	elif rad_cal7 <=30:
		url = "https://www.linkedin.com/"
		webbrowser.open(url, new = 2)
		return 7
	elif rad_cal8 <=30:
		# Rest are left for customisation :)
		return 8
	elif rad_cal9 <=30:
		return 9
	elif rad_cal10 <=30:
		return 10 
	elif rad_cal11 <=30:
		return 11
	else:
		return 13



def operate(frame):
	frame_copy = frame.copy()
	frame_ret = frame.copy()

	# Converting the frame from BGR format to HSV format, Hue Saturation Value GOOGle for more
	frame_copy = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

	# Thresholding the frame to get our object(color) tracked
	mask = cv2.inRange(frame_copy, byju_min, byju_max)
	mask = cv2.medianBlur(mask, 5)
	mask = cv2.erode(mask, None, iterations=2)
	mask = cv2.dilate(mask, None, iterations=2)

	mask1 = cv2.inRange(frame_copy, hsv_infinum, hsv_supremum)
	mask1 = cv2.medianBlur(mask1, 5)
	mask1 = cv2.erode(mask1, None, iterations=2)
	mask1 = cv2.dilate(mask1, None, iterations=2)


	#cv2.imshow('mask', mask)

	# Obtaining the binary Image for the corresponding mask
	res = cv2.bitwise_and(frame_ret,frame_ret, mask= mask)
	res1 = cv2.bitwise_and(frame_ret,frame_ret, mask= mask1)


	#cv2.imshow('res', res)
	# The result can be improved by smoothening the frame

	mask_copy = mask.copy()
	mask1_copy = mask1.copy()

	cnts = cv2.findContours(mask.copy(), cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)[-2]
	cnts1 = cv2.findContours(mask1.copy(), cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)[-2]

	center = None
	center1 = None

	if len(cnts) > 0:
		c = max(cnts, key = cv2.contourArea)
		((x, y), radius) = cv2.minEnclosingCircle(c)

		M = cv2.moments(c)
		center = (int(M["m10"] / M["m00"]), int(M["m01"] / M["m00"]))

		if radius > 10:
			#der1_cord = ((int(x)-30,int(y)-10),(int(x)+10,int(y)+30))

			cv2.rectangle(frame_ret,(int(x)-30,int(y)-10),(int(x)+10,int(y)+30),(0,255,0),3)
			cv2.circle(frame_ret, center, 5, (0, 0, 255), -1)

	if len(cnts1) > 0:
		c1 = max(cnts1, key = cv2.contourArea)
		((x1, y1), radius1) = cv2.minEnclosingCircle(c1)

		M1 = cv2.moments(c1)
		center1 = (int(M1["m10"] / M1["m00"]), int(M1["m01"] / M1["m00"]))

		if radius1 > 10:
			#der2_cord = ((int(x1)-30,int(y1)-10),(int(x1)+10,int(y1)+30))

			cv2.rectangle(frame_ret, (int(x1)-30,int(y1)-10), (int(x1)+10,int(y1)+30),(150, 0, 255), 3)
			cv2.circle(frame_ret, center1, 5, (0, 255, 255), -1)

	pts.appendleft(center)
	pts1.appendleft(center1)
	#print(pts[0])
	
	'''for i in range(1 , len(pts)):
		if (pts[i-1] is None or pts[i] is None):
			continue
		
		for j in range(1, len(pts1)):
			if (pts1[i-1] is None or pts1[i] is None):
				continue
			dist = (pts[i][0]-pts1[i][0])**2 + (pts[i][1]-pts1[i][1])**2
			dist = dist**0.5'''#
	
	if pts[0] is not None and pts1[0] is not None:
		print('jols')
		dist = (pts[0][0]-pts1[0][0])**2 + (pts[0][1]-pts1[0][1])**2
		dist = dist**0.5
		if dist<=60:
			der_cenetre = (int((pts[0][0]+pts1[0][0])/2), int((pts[0][1]+pts1[0][1])/2))
			cv2.circle(frame_ret, der_cenetre, 100, (0, 255, 0), 6)
			cv2.circle(frame_ret, der_cenetre, 2, (255, 255, 255), -1)
			
			return frame_ret, radcal(der_cenetre)
			

			


	return frame_ret, 13

	



while True:
	# Let's capture/read the frames for the video
	_, frame = cap.read() 

	# This frame captured is flipped, Lets's get the mirror effect
	frame = cv2.flip(frame, 1)

	# Any operations on the frame captured will be processed in the operate(frame) method
	final_frame, count = operate(frame)
	make_circle1(final_frame, count)

	# Showing the captured frame
	cv2.imshow('garrix', frame)
	cv2.imshow('martin', final_frame)

	# Continuous, Large amount of frames produce a video
	# waitkey(value), value is the ammount of millisecs a frame must be displayed
	# 0xff represents the ASCII value for the key, 27 is for ESC
	# waitKey is necessary to show a frame
	if cv2.waitKey(1) & 0xff == 27:
		break


cap.release()
cv2.destroyAllWindows()