# TrackerExpress

###Note!  
Doing a fresh clone on any machine other than a lab mmachine might require you (in eclipse) to go to:  
_Project_=>_Properties_=>_Java Build Path_=>_Order and Export_ and uncheck **_android-support-v4.jar_**

The included google-play-services_lib needs to be imported into the workspace, then go to:  
_Project_=>_Properties_=>_Android_=>_Add..._ and select **_google-play-services_lib_**

The target API is 17, but in order to test map functionality, API 19 must be used. Go to:  
_Project_=>_Properties_=>_Android_ and select the _Build Target_ to be **_Google APIs 4.4.2_**
