# kinit-app-android
Kin is set out to change the digital world and the way people experience and exchange value online. 
Kinit is a mobile app with a standalone Kin experience. 
This repository hosts the code for the Kinit android app.

## Build
Our code is open source, however some information, (such as our google-services.json and app signing keys) 
are not included so you won't be able to build the Kinit app on your own.
You are welcome though to read through the code and use it as an example to build your own app using the KIN token.

## Integration with kin-core SDK
We use the [kin-core-android](https://github.com/kinecosystem/kin-core-android) SDK to create a KIN wallet for users, 
check for balance and create spending transactions (either to pay KIN for an offer or to transfer KIN to a friend).
The Kinit private beta is working over the Stellar public testnet. We will move over to the Kin public blockchain 
(Stellar fork) for our public beta which is planned in mid July.

![Kin Token](kin_android.png)