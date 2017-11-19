// IteraLib.h

#ifndef _ITERALIB_h
#define _ITERALIB_h

#if defined(ARDUINO) && ARDUINO >= 100
	#include "Arduino.h"
#else
	#include "WProgram.h"
#endif

#include <ESP8266WiFi.h>
#include <PubSubClient.h>

#define DEBUG

class Itera
{
private:
	WiFiClient wifiClient;
	PubSubClient mqttClient;
	const char* mqtt_id;
	const char* mqtt_user;
	const char* mqtt_pass;

 protected:


 public:
	 void setupSerial(int bautrate);

	 void setup_wifi(const char* ssid, const char *passphrase = NULL);
	 WiFiClient* getClientWifi();

	 void reconnectMQTT();
	 void setupMQTTServer(IPAddress ip, uint16_t port);
	 void setupMQTTServer(const char * domain, uint16_t port);
	 void setupMQTTClient(const char* id);
	 void setupMQTTClient(const char* id, const char* user, const char* pass);
	 boolean publishMQTT(const char* topic, const char* payload);
	 PubSubClient* getClientMQTT();

	 void deepSleep_us(uint32_t time_us, RFMode mode = RF_DEFAULT);
	 void deepSleep_ms(uint32_t time_ms, RFMode mode = RF_DEFAULT);
	 void deepSleep_sec(uint32_t time_sec, RFMode mode = RF_DEFAULT);

	 int getBatteryStatus(int analogPin);

	 bool webUnixTime(char* time); //const char * server = "google.de");
	 //unsigned long webUnixTime(const char * server , int port);
};

extern Itera IteraLib;

#endif

