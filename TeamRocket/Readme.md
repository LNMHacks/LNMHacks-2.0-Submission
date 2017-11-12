
# TEAM ROCKET SUBMISSION

Team Rocket includes
> @heyayushh
> @johermohit
> @vipul02
> @

## Contributing [![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/dwyl/esta/issues)

[![HitCount](http://hits.dwyl.io/heyAyushh/rocket.svg)](http://hits.dwyl.io/heyAyushh/rocket)

IoT and Home Automation for Differently Abled Person

The objective of the hack was to build a concise and failsafe IoT approach for developers and users to develop and automate the ease for Differently Abled people.

Dreamt -

[x]Initialising a basic HomeAutomation Hardware which can be Integrated without changing the basic House wiring.
[x]Initialing Google Now Commands to assist and Toggle/Control via Voice Commands.
[ ]Integrating ML for Real Time Prediction

Built -

We successfully implemented the first two things we dreamt and planned for.

Future Plans -

[ ] Docker file for Raspberry Pi broker so that it can be deployed without investing much time on setting configs
[ ] Integrating ML in it
[ ] A chatbot with NLP so that it can Toggle/Control via Natural Language
[ ] Integrating support for people with motor neurone disease via Servos, Vibration sensors, Heart rate sensors and lot more.

---------------------------------------------------------------------------------------------------------------------

# Setting Up your Environment


### RPi Setup

Run these commands in terminal 

```
sudo apt-get install node-red
```

Clone this [Repo](https://github.com/heyAyushh/Rocket) to your Pi
```git clone https://github.com/heyAyushh/Rocket.git```

Setup a Startup script
	so that you don't have to run it manually and it launches on startup
    
   1. Get Root Access
   
		```
		sudo -i
		```

   2. copy the contents of [runatstartup.conf](https://github.com/heyAyushh/Rocket/blob/master/runatstartup.conf)
		
        ```
	 	 cp /home/pi/Rocket/runatstartup.conf /etc/init
        ```
 


   3. Run Command
	``` node-red-start ```

   4. Import flows form Rocket/flow 
        
### VOILA Start Hacking..
    
    
