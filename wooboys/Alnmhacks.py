import cv2
import numpy as np 

from collections import deque

pts = deque()
cap = cv2.VideoCapture(0)

# Info : ZOOM in the frame to get the RGB value of the corresponding pixel

# The range of color for the object to be detected in HSV
hsv_supremum = np.array([220, 221, 255])
hsv_infinum = np.array([150, 100, 130])

def make_circle1(frame, fill1=0):
	frame_copy = frame.copy()
	
	alpha = 0.9

	cv2.circle(frame_copy,(500,500), 40, (96,96,96), fill1)
	cv2.line(frame_copy, (500,500-10), (500, 500+10), (0,0,0), 2) 
	cv2.line(frame_copy, (500-10,500), (500+10, 500), (0,0,0), 2)

	frame_ret = cv2.addWeighted(frame, 1-alpha, frame_copy, alpha, 0)


	cv2.imshow('final', frame_ret)

def make_circle2(frame, fill):
	frame_copy = frame.copy()

	alpha = 0.9

	cv2.circle(frame_copy,(400,400), 40, (96,96,96), fill1)
	cv2.line(frame_copy, (400,400-10), (400, 400+10), (0,0,0), 2) 
	cv2.line(frame_copy, (400-10,400), (400+10, 400), (0,0,0), 2)

	cv2.circle(frame_copy,(600,600), 40, (96,96,96), fill1)
	cv2.line(frame_copy, (600,600-10), (600, 600+10), (0,0,0), 2) 
	cv2.line(frame_copy, (600-10,600), (600+10, 600), (0,0,0), 2)
	frame_ret = cv2.addWeighted(frame, 1-alpha, frame_copy, alpha, 0)

	cv2.imshow('final2', frame_ret)

def operate(frame):
	frame_copy = frame.copy()
	frame_ret = frame.copy()


	# Converting the frame from BGR format to HSV format, Hue Saturation Value GOOGle for more
	frame_copy = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

	# Thresholding the frame to get our object(color) tracked
	mask = cv2.inRange(frame_copy, hsv_infinum, hsv_supremum)
	mask = cv2.medianBlur(mask, 5)
	mask = cv2.erode(mask, None, iterations=2)
	mask = cv2.dilate(mask, None, iterations=2)

	#cv2.imshow('mask', mask)

	# Obtaining the binary Image for the corresponding mask
	res = cv2.bitwise_and(frame_ret,frame_ret, mask= mask)

	#cv2.imshow('res', res)
	# The result can be improved by smoothening the frame

	mask_copy = mask.copy()
	cnts = cv2.findContours(mask.copy(), cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)[-2]
	center = None

	if len(cnts) > 0:
		c = max(cnts, key = cv2.contourArea)
		((x, y), radius) = cv2.minEnclosingCircle(c)

		M = cv2.moments(c)
		center = (int(M["m10"] / M["m00"]), int(M["m01"] / M["m00"]))

		if radius > 10:
			cv2.circle(frame_ret, (int(x), int(y)), int(radius), (0, 150, 255), 2)
			cv2.circle(frame_ret, (int(x), int(y)), int(radius+10), (255, 0, 255), 2)

			#cv2.circle(frame_ret, center, 5, (0, 0, 255), -1)
			cv2.line(frame_ret, (int(x)-10, int(y)),  (int(x)+10, int(y)), (0, 0, 0), 1)
			cv2.line(frame_ret, (int(x), int(y)-10),  (int(x), int(y)+10), (0, 0, 0), 1)
			

	pts.appendleft(center)

	
	
	#print(pts[0])
	fill1 = 0
	
	for i in range(1 , len(pts)):
		if pts[i-1] is None or pts[i] is None:
			make_circle1(final_frame)
			continue
		rad_cal = (pts[i][0]-500)**2 + (pts[i][1]-500)**2
		rad_cal = rad_cal**0.5

		

		if rad_cal <= 30:
			print('Hola, WE found')
			fill1 = -1	

	return frame_ret, fill1

	



while True:
	# Let's capture/read the frames for the video
	_, frame = cap.read() 

	# This frame captured is flipped, Lets's get the mirror effect
	frame = cv2.flip(frame, 1)

	# Any operations on the frame captured will be processed in the operate(frame) method
	final_frame, fill1 = operate(frame)
	
	# Showing the captured frame
	#cv2.imshow('garrix', frame)
	#cv2.imshow('martin', final_frame)
	make_circle1(final_frame, fill1)
	#make_circle2(final_frame, 1)
	# Continuous, Large amount of frames produce a video
	# waitkey(value), value is the ammount of millisecs a frame must be displayed
	# 0xff represents the ASCII value for the key, 27 is for ESC
	# waitKey is necessary to show a frame
	if cv2.waitKey(1) & 0xff == 27:
		break


cap.release()
cv2.destroyAllWindows()