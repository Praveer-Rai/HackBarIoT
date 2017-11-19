// 
// 
// 

#include "IteraLib.h"



void Itera::setup_wifi(const char * ssid, const char * passphrase)
{
	delay(10);
	// We start by connecting to a WiFi network
	Serial.println();
	Serial.print("Connecting to ");
	Serial.println(ssid);

	if (WiFi.status() != WL_CONNECTED) {
		//This fix the problem with undefined wifi states after a lot off deepsleep and reset
		WiFi.persistent(false);
		WiFi.mode(WIFI_OFF);   // this is a temporary line, to be removed after SDK update to 1.5.4
		WiFi.mode(WIFI_STA);
		WiFi.begin(ssid, passphrase);
	}

	while (WiFi.status() != WL_CONNECTED) {
		delay(500);
		Serial.print(".");
	}

	randomSeed(micros());

#ifdef DEBUG
	Serial.println("");
	Serial.println("WiFi connected");
	Serial.print("MAC adress:\t");
	Serial.println(WiFi.macAddress());
	Serial.print("IP address:\t");
	Serial.println(WiFi.localIP());
	Serial.print("SubnetMask:\t");
	Serial.println(WiFi.subnetMask());
	Serial.print("Gateway:\t");
	Serial.println(WiFi.gatewayIP());
	Serial.println();
#endif // DEBUG
}

WiFiClient * Itera::getClientWifi()
{
	return &wifiClient;
}

void Itera::reconnectMQTT()
{	
	
		// Loop until we're reconnected
	while (!mqttClient.connected()) {
		Serial.print("Attempting MQTT connection to ");
		// Create a random client ID
		String clientId = mqtt_id + String(random(0xffff), HEX);
		// Attempt to connect
		if (mqtt_user) {
			if (mqttClient.connect(clientId.c_str(), mqtt_user, mqtt_pass)) {
				Serial.println("connected");
			} else {
				Serial.print("failed. rc=");
				Serial.print(mqttClient.state());
				Serial.println(" try again in 5 seconds");
				// Wait 5 seconds before retrying
				delay(5000);
			}
		} else {
			if (mqttClient.connect(clientId.c_str())) {
				Serial.println("connected");
			}
			else {
				Serial.print("failed, rc=");
				Serial.print(mqttClient.state());
				Serial.println(" try again in 5 seconds");
				// Wait 5 seconds before retrying
				delay(5000);
			}
		}
	}
	//This pings the server to hold the connection
	mqttClient.loop();
}

void Itera::setupMQTTServer(IPAddress ip, uint16_t port)
{
	mqttClient.setServer(ip, port);
}

void Itera::setupMQTTServer(const char * domain, uint16_t port)
{
	mqttClient.setClient(wifiClient);
	mqttClient.setServer(domain, port);
}

void Itera::setupMQTTClient(const char * id)
{
	mqtt_id = id;
}

void Itera::setupMQTTClient(const char * id, const char * user, const char * pass)
{
	mqtt_id = id;
	mqtt_user = user;
	mqtt_pass = pass;
}

boolean Itera::publishMQTT(const char * topic, const char * payload)
{
	
	bool result = mqttClient.publish(topic, payload);
#ifdef DEBUG
	if (!result) {
		Serial.print("Fail to publish -> State: ");
		Serial.println(getClientMQTT()->state());
	}
	else {
		Serial.print("Success to publish -> State: ");
		Serial.println(getClientMQTT()->state());
	}
#endif // DEBUG

	return result;
}

PubSubClient* Itera::getClientMQTT()
{
	return &mqttClient;
}

void Itera::deepSleep_ms(uint32_t time_ms, RFMode mode)
{
	deepSleep_us(time_ms * 1000, mode);
}

void Itera::deepSleep_sec(uint32_t time_sec, RFMode mode)
{
	deepSleep_ms(time_sec * 1000, mode);
}

int Itera::getBatteryStatus(int analogPin)
{
	//Check battery
	int level = analogRead(analogPin);
	level = map(level, 512, 1024, 0, 100);
#ifdef DEBUG
	Serial.print("battery: ");
	Serial.println(level);
#endif // DEBUG


	if (level < 50) {

		Serial.println("Low battery");

#ifdef DEEPSLEEP
		Serial.println("DeepSleep");
		deepSleep_sec(604800);// Sleep for one Week
		delay(500);
#endif
		
	}
	return level;
}
/*
unsigned long Itera::webUnixTime(const char * server) {
	webUnixTime(server, 8192000);
}
*/

bool Itera::webUnixTime(char* time)
{
	
	getClientWifi()->stopAll();

		// Just choose any reasonably busy web server, the load is really low
		int status = getClientWifi()->connect(IPAddress(192,168,1,100), 19200);
		Serial.printf("Connection Status: %u \n", status);
		
		if (status)
		{
			getClientWifi()->print(F("GET /_nodes/stats HTTP/1.1 \r\n\r\n"));
		
			getClientWifi()->setTimeout(5000);
			
			if (getClientWifi()->find((char *)"{\"timestamp\":")) {
				int i = 0;
				while (i < 13) {
					if (getClientWifi()->available()) {
						time[i] = getClientWifi()->read();
						Serial.print(time[i]);
						++i;
					}
				}
				time[13] = '\0';
			}
	
		}
		delay(10);
		getClientWifi()->flush();
		getClientWifi()->stop();

	return true;
}

void Itera::deepSleep_us(uint32_t time_us, RFMode mode)
{
	ESP.deepSleep(time_us, mode);
}

void Itera::setupSerial(int bautrate) {
	Serial.begin(bautrate);

#ifdef DEBUG
	Serial.println();
	Serial.println(ESP.getResetReason());
	Serial.println(ESP.getResetInfo());
#endif // DEBUG
}

Itera IteraLib;