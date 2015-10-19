# Citizen Science App

Citizen Science is a software layer for a distributed computing system using a pool of android devices. It uses a coordinator/server and a pool of android devices running the [Citizen Science app](https://github.com/Philip-Lawson/Client-App) to do the actual computation. It is designed for computation that is linearly scalable/embarrassingly parallel.

The Coordinator
* Sends data packets to devices
* Validates packets retrieved from devices
* Protects against fraudulent devices
* Visualises the progress of the computation
* Stores the processed data
* Dynamically reloads any data that hasn't been processed
* Allows the user to change paramaters during the computation, including  
 * The level of redundancy in the computation (duplicate packets)
 * The amount of data sent to each device (number of packets sent)
 * The percentage of reliability required to keep a device in the computation
 * The time-limit before a device is considered inactive
* Manages client registration/deregistration requests
* Sends congratulatory emails to users that have registered with their email address

The Client App
* Processes data in the background while the device is operational
* Requests data to be processed, and returns processed data to the coordinator
* Allows the user to start and stop processing at will
* Stops processing when the battery drops below a certain level
* Restarts processing when the battery is charging/above a certain level
* Respects the user's network preferences
* Enters a dormant state when there is no more data to be processed
* Allows a user to register/unregister their device from the computation
* Allows the user to donate to the project from an external web page
* Allows the user to change/delete their email address in the system

## Implementing the app
To implement the app you will need a Google Play account.

Before loading it to the Google Play store you must
* Extend the AbstractDataProcessor class, implement the processData method and plug this into the Implementations class (src/client/implementations/)
* Set the isDemo option to false
* Set the host URL and port as appropriate to communicate with the Coordinator.
* If you want to link the user to a donate page, set donateButtonEnabled to return true and set getProjectWebsite to return the URL of your website.
* Write the about text for the app and include it in the Implementations class.
* Change the name of the app in the res/values/strings file
* Change the package name in the manifest to something sensible

## Example DataProcessor Implementation

```Java
public class TestProcessor extends AbstractDataProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1696565467124647685L;

	@Override
	protected Serializable processData(Serializable obj) {
    
         // cast the Serializable to the object that you need to process
         // here we're just simulating a long calculation
		Integer number;
		try {
			number = (Integer) obj;
			
			// long processing method
			Thread.sleep(10000);
			return number * 2;
		} catch (ClassCastException e) {
			return -1;
		} catch (InterruptedException e) {
			return -1;
		}

	}

}
```
