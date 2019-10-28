University of Melbourne
School of Computing and Information Systems
Masters Software Engineering Project (SWEN90014)

Demo video




[![IMAGE ALT TEXT](http://img.youtube.com/vi/Sv3x6ugXt_g/0.jpg)](http://www.youtube.com/watch?v=Sv3x6ugXt_g "Video Title")




Crime Reporting System
Year: 2018

Team Members:
Anthony Miller
Aymen Al-Quaiti
Emily Ha
Sathitsak Anawatmongkol 
Savan Kanabar

Under Supervision of:
Nwakego Isika
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Built With

FrontEnd Mobile
	JDK
	React Native
	Watchman
	Xcode

FrontEnd Webapp
    Node JS
    HTML
    CSS
    Handlebars
    Axios
    
BackEnd
	jOOQ
    Maven
    Junit
    MySQL

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

/*React Native*/

The react native's installation is dependent on the operating system and the target mobile platform. This document includes setup instructions for macOS targeting iOS platform and windows targeting Android platform.

React Native on MacOS
Targeting  iOS

Install Xcode
Make sure to have the latest version of macOS and Xcode to avoid build errors. Expected version is 9.4 and up

Homebrew
Go to https://brew.sh/ for instructions

Node JS
within a terminal, write the following:
brew install nodejs
To confirm installation, type:
node -v

Watchman
brew install watchman
Watchman needs python2 as a dependency, but macOS comes preinstalled with it.

React Native Command Line Interface
npm install -g react-native-cli
ESLint (Optional):
Installing ESLint will help to ensure a smooth coding exprience. Type the following command:
npm install -g eslint


Note:

Before running the simulator for the first time, make sure that you run Xcode for the first time. This will run some scripts and activate tools the simulator needs. After running your xcode, from top menu choose Xcode → Preferences. Under general tap, choose command line tools to be Xcode 9.4.#, where # is a minor number.

Running iOS Simulator

Go the project directory and run the following command:

react-native run-ios

React Native on Windows

Creating a new Project
This step is only mentioned for documentation purposes and is not to be performed, as the source code can be accessed through Bitbucket.

Instal Node.js https://nodejs.org/en/download/

Install react-native-cli by using  npm (Node.js packages, or modules) in command line 
npm install -g react-native-cli

To initialise a new project, go to the desired directory in terminal and type the following command:
react-native init project-name

Move direction in command line to project folder and start npm
cd project-name 

Note:

Using  react-native init project-name instead of create-react-native-app AwesomeProject in order to create that tha could ask permision to use 3rd party APK, which unliked to Expo framework.

Running Android Simulator

Go the project directory and run the following command:

react-native run-android

Before using the android simulator

Make sure that you have Android SDK install in your machine, through Android Studio 
You have the same JDK and JRE version in your machine
Press Ctrl+M to bring dev menu
Troubleshooting

500 error with red screen (bug with new react native on android )
To fix this change react-native version in package.json file of your project to 0.55.4 and react version to 16.3.1 and babel-preset-react-native to 2.1.0
Then delete your node_modules file in project directory.
Run npm install --save to install node_modules in cmd again. 


go to Android Studio, then click Build/Rebuild
run react-native run-android in cmd again
Debug

ctrl + m → debug
change url to http://localhost:8081/debugger-ui/ 
go to console tab
--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
Running the tests
Explain how to run the automated tests for this system

Break down into end to end tests
Explain what these tests test and why

Deployment
Add additional notes about how to deploy this on a live system


# CrimeReportReactNative
