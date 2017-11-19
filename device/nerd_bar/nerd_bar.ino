
# define DEV_ID "1052512258"//Device ID (originally ...59)
volatile int rgb_state=0; //2 = find_my_drink // 1= Need assistance // 0=normal temp light

//FREQUENCIES!!!!
# define ACC_FREQ 1
# define TEMP_FREQ 10
# define RGB_FREQ 5
# define API_FREQ 248

//#include <math.h>

# define SIP_THRESHOLD 45 //Percent over which a sip is registered
# define MIN_SIP_DUR 15 //Minimum cycles to consider a sip

# define T_TRANSITION_ZONE 0.2//% of the T range where the color starts changing

# define MAX_ACC 20000


void getGyro(void);
void getAccel(void);
//void send_temp_to_api(float temp);

//INTERRUPTS HEADER
/*---------------------------------------------------------------------------------------*/
//#define TEMP_ISR_INT 3000000//5000000//40000000//80000000
/*---------------------------------------------------------------------------------------*/


//ACCELEROMETER
/*---------------------------------------------------------------------------------------*/
#include <Wire.h>// requires I2Cdev library: https://github.com/jrowberg/i2cdevlib
#include <I2Cdev.h>// requires MPU-6050 part of the I2Cdev lib: https://github.com/jrowberg/i2cdevlib/tree/master/Arduino/MPU6050

#include <MPU6050.h>

int sip_cycle_counter=0;
int tot_sips=0;

float ar,atot;
float phi=0;

//Previous value
float ax_p1=0;
float ay_p1=0;
float az_p1=0;

//Before the previous value

//Smoothing factors THEIR SUM MUST BE LESS THAN 1
float s_f1=0.5;//Between 0 and 1

///////////////////////
// MPU6050 Setup //
///////////////////////
// class default I2C address is 0x68
// specific I2C addresses may be passed as a parameter here
// AD0 low = 0x68 (default for InvenSense evaluation board)
// AD0 high = 0x69
MPU6050 accelgyro;
//MPU6050 accelgyro(0x69); // <– use for AD0 high
// uncomment "OUTPUT_READABLE_ACCELGYRO" if you want to see a tab-separated
// list of the accel X/Y/Z and then gyro X/Y/Z values in decimal. Easy to read,
// not so easy to parse, and slow(er) over UART.
#define OUTPUT_READABLE_ACCELGYRO

int16_t ax, ay, az;
int16_t gx, gy, gz;

//long sendCount = 0;
//long frameCount = 0;

void getGyro();
void getAccel();
/*---------------------------------------------------------------------------------------*/


//TEMP HEADER
/*---------------------------------------------------------------------------------------*/
//Include TEMP libraries
#include <OneWire.h>
#include <DallasTemperature.h>
// Data wire is plugged into pin 2 on the Arduino
#define ONE_WIRE_BUS D4
// Setup a oneWire instance to communicate with any OneWire devices (not just Maxim/Dallas temperature ICs)
OneWire oneWire(ONE_WIRE_BUS);
// Pass our oneWire reference to Dallas Temperature. 
DallasTemperature sensors(&oneWire);

float current_temp=20;
float min_temp=25;
float max_temp=30;

float get_temp(void){
  sensors.requestTemperatures();  
  return sensors.getTempCByIndex(0);}

void isr_routine_0(void){
  //Serial.println("IN THE ISR");

    //Serial.println("Before getting accs");
    getAccel(); 
    //FOR THE SERIAL PLOTTER
    //Serial.println(phi);
    /*Serial.print(ax);Serial.print(" ");
    Serial.print(ay);Serial.print(" ");
    Serial.println(az);*/
    

    if(phi<SIP_THRESHOLD){
      sip_cycle_counter=0;
    } else if(phi>SIP_THRESHOLD){
        sip_cycle_counter+=1;
        
        if(sip_cycle_counter>=MIN_SIP_DUR){//ONE SIP!
          //CALL PRAVEERS API FOR ADDING A SIP
          
          sip_cycle_counter=0;
          tot_sips+=1;
          
          }
        
    }
    
  
/*
  switch (rgb_state) {
  case 2:
    // Find my drink
    rainbow();
    break;
  case 1:
    // Need assistance
    alarm_rgb();
    break;
  default:
    // Light according to temp
    scale_temp(current_temp);
  }*/

  /*Serial.println(current_temp);
 
  
  */

  //timer0_write(ESP.getCycleCount() + TEMP_ISR_INT);//Prepare next ISR call
  }

/*---------------------------------------------------------------------------------------*/


//RGB HEADER
/*---------------------------------------------------------------------------------------*/
#include <Adafruit_NeoPixel.h>
#ifdef __AVR__
  #include <avr/power.h>
#endif

int wheel_idx=0;
int wheel_dir=1;
int alarm_toggle=1;


#define ALARM_INT 2

#define PIN D6
#define NUM_LEDS 1
#define BRIGHTNESS 50

Adafruit_NeoPixel strip = Adafruit_NeoPixel(NUM_LEDS, PIN, NEO_GRBW + NEO_KHZ800);

// Color the RGB
void rgb_color(uint32_t c) {
    strip.setPixelColor(0, c);
    strip.show();
}

// Input a value 0 to 255 to get a color value.
// The colours are a transition r - g - b - back to r.
uint32_t Wheel(byte WheelPos) {
  WheelPos = 255 - WheelPos;
  if(WheelPos < 85) {
    return strip.Color(255 - WheelPos * 3, 0, WheelPos * 3,0);
  }
  if(WheelPos < 170) {
    WheelPos -= 85;
    return strip.Color(0, WheelPos * 3, 255 - WheelPos * 3,0);
  }
  WheelPos -= 170;
  return strip.Color(WheelPos * 3, 255 - WheelPos * 3, 0,0);
}

void rainbow(void){
  if(wheel_idx>255){
    wheel_dir=-1;
    wheel_idx=255;
  }else if (wheel_idx<0){
    wheel_dir=+1;
    wheel_idx=0;
   }
    
  //Serial.print("in rainbow, idx= ");
  //Serial.println(wheel_idx);
  strip.setPixelColor(0, Wheel((wheel_idx) & 255));
  strip.show();

  wheel_idx+=wheel_dir*50;
}

void alarm_rgb(void){
  if((alarm_toggle<ALARM_INT)&&(alarm_toggle>0)){
      rgb_color(strip.Color(255, 0, 0)); // Red 
   }else{
      if(alarm_toggle==ALARM_INT){alarm_toggle=-ALARM_INT+1;}
      rgb_color(strip.Color(0, 0, 0)); // OFF
    }
    alarm_toggle+=1;
    }
  

void scale_temp(float t){
  float t_span=(max_temp -min_temp)*T_TRANSITION_ZONE;//Size of the color transition interval

  if((t>max_temp)||(t<min_temp)){//DRINK IS ABOVE MAX TEMP or UNDER MIN TEMP
    
     rgb_color(strip.Color(255, 0, 0)); // Red 
     
   }else if (t>(max_temp-t_span)){//DRINK IS CLOSE TO A LIMIT
    float red_percent = min(abs(t-min_temp),abs(max_temp))/t_span;
    rgb_color(strip.Color(255*(red_percent), 255*(1-red_percent), 0)); // COLOR MIX
   }else if(t<(min_temp+t_span)){
    float red_percent = min(abs(t-min_temp),abs(max_temp))/t_span;
    rgb_color(strip.Color(255*(1-red_percent), 255*(red_percent), 0)); // COLOR MIX
   }else{
    rgb_color(strip.Color(0, 255, 0)); // Green PERFECT DRINK
  }
}

/*---------------------------------------------------------------------------------------*/


//API HEADER
/*---------------------------------------------------------------------------------------*/
#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266HTTPClient.h>
#define USE_SERIAL Serial
ESP8266WiFiMulti WiFiMulti;

void send_to_api(float temp){

    //millis connect timeout TODO
     
    if((WiFiMulti.run() == WL_CONNECTED)) {// Check for WiFi connection
    
      HTTPClient http;
      http.begin("http://hackatumdemoapp.azurewebsites.net/nerdbar/temperature");
      //http.addHeader("Content-Type", "application/x-www-form-urlencoded");
      
      String s_to_send;
      s_to_send.concat("deviceId=");
      s_to_send.concat(DEV_ID);
      s_to_send.concat("&temp=");
      s_to_send.concat(String((int)(temp)));

      //Serial.println(s_to_send);
      http.POST(s_to_send);
   
      
      String api_response=http.getString();
      Serial.println(api_response);
      
      http.end();
  }

}

void get_from_api(void){

    //millis connect timeout TODO
     Serial.println("Before if statement");
    if((WiFiMulti.run() == WL_CONNECTED)) {// wait for WiFi connection
    
      HTTPClient http;
      //Build the url
      String url="http://hackatumdemoapp.azurewebsites.net/nerdbar/getDrinkInfo";
      url.concat("?deviceId=");
      url.concat(DEV_ID);
      
      //Get the response
      http.begin(url);
      Serial.println(url);
      
      //http.addHeader("Content-Type", "application/x-www-form-urlencoded");
      int httpCode = http.GET();
      String api_response=http.getString();

      Serial.println(api_response);
  
      int del_idx = api_response.indexOf(':');
      int del_idx_2 = api_response.indexOf(',', del_idx + 1);
      int del_idx_3 = api_response.indexOf(',', del_idx_2 + 1);
      
      float min_t = api_response.substring(0, del_idx).toFloat();
      float max_t = api_response.substring(del_idx + 1, del_idx_2).toFloat();
      int find_my_drink = api_response.substring(del_idx_2, del_idx_3).toInt();
      int need_assistance = api_response.substring(del_idx_3+1).toInt();

      /*
      Serial.print("Received vals: ");        
      Serial.print(min_t);Serial.print(" :  ");    
      Serial.print(max_t);Serial.print(" :  ");  
      Serial.print(find_my_drink);Serial.print(" :  ");    
      Serial.print(need_assistance);Serial.print(" AND==  ");*/
      Serial.println((min_t==0.0)&&(max_t==0.0)&&(find_my_drink==0)&&(need_assistance==0));      

      if((min_t==0.0)&&(max_t==0.0)&&(find_my_drink==0)&&(need_assistance==0)){
        //Response did not answer
        Serial.println("Empty response from the server!");
 
      }else{//Got some sort of answer

        min_temp=min_t;
        max_temp=max_t;
        
        //Find the rgb state
        if(find_my_drink==1){
          rgb_state=2;// = Need assistance
        }else if(need_assistance==1){
          rgb_state=1;// = Find my drink
        }else{
          rgb_state=0;// = Normal temp color
        }       
      }
      
      http.end();
  
  }

}

/*---------------------------------------------------------------------------------------*/


void setup(void)
{
  //noInterrupts();
  //Serial setup
  Serial.begin(38400); //Begin serial communication//OLD VAL=9600 //ACC=38400

  //TEMP setup
  /*---------------------------------------------------------------------------------------*/
  sensors.begin();
  /*---------------------------------------------------------------------------------------*/

  //RGB SETUP
  /*---------------------------------------------------------------------------------------*/
    // This is for Trinket 5V 16MHz, you can remove these three lines if you are not using a Trinket
  //#if defined (__AVR_ATtiny85__)
  //  if (F_CPU == 16000000) clock_prescale_set(clock_div_1);
  //#endif
  // End of trinket special code
  strip.setBrightness(BRIGHTNESS);
  strip.begin();
  strip.show(); // Initialize all pixels to 'off'
  /*---------------------------------------------------------------------------------------*/

  //API SETUP
  /*---------------------------------------------------------------------------------------*/

      //USE_SERIAL.begin(9600);//OLD:115200
   // USE_SERIAL.setDebugOutput(true);

    for(uint8_t t = 4; t > 0; t--) {
        USE_SERIAL.printf("[SETUP] WAIT %d...\n", t);
        USE_SERIAL.flush();
        //delay(1000);//10000 //TODO uncomment
    }
    //WiFiMulti.addAP("fmi-events", "hack?-17"); //Bad wifi
      WiFiMulti.addAP("iot<1d", "tick4Loo"); //Bad wifi
    //WiFiMulti.addAP("hackaTUM-5G", "hackatum");

  /**/



  /*---------------------------------------------------------------------------------------*/
  
    //ACCELEROMETER
  /*---------------------------------------------------------------------------------------*/
//pinMode(0, OUTPUT); 
//digitalWrite(0, HIGH);

// join I2C bus (I2Cdev library doesn’t do this automatically)
#if I2CDEV_IMPLEMENTATION == I2CDEV_ARDUINO_WIRE

Wire.begin(D2, D1);//pins 2 and 14 are SDA and SCL, but you can manually set them to any other pin by calling Wire.begin([SDA], [SCL])
#elif I2CDEV_IMPLEMENTATION == I2CDEV_BUILTIN_FASTWIRE
Fastwire::setup(400, true);
#endif

// (38400 chosen because it works as well at 8MHz as it does at 16MHz, but
// it’s really up to you depending on your project)
//Serial.begin(38400);

// initialize device
Serial.println("Initializing I2C devices…");
accelgyro.initialize();

// verify connection
Serial.println("Testing device connections…");
Serial.println(accelgyro.testConnection() ? "MPU6050 connection successful" : "MPU6050 connection failed");

/*---------------------------------------------------------------------------------------*/


//INTERRUPTS
//*---------------------------------------------------------------------------------------*/
  /*
  noInterrupts();//At the top!
  timer0_isr_init();
  timer0_attachInterrupt(isr_routine_0);
  timer0_write(ESP.getCycleCount()+TEMP_ISR_INT);
  interrupts();//TODO reactivate the ISR for the LED
  */
  /*---------------------------------------------------------------------------------------*/

 


}

void loop(void)
{ 

   for (int ct=0; ct <= 1000000; ct++){

//ACCELERATION
/////////////////////////////////////////////////////////////////
  if((ct%ACC_FREQ)==0){update_acc();}

  /*Serial.print("count : "); Serial.print(sip_cycle_counter);
  Serial.print("SIPS : "); Serial.println(tot_sips);*/
  
  
//TEMPERATURE
/////////////////////////////////////////////////////////////////
if((ct%TEMP_FREQ)==0){current_temp=get_temp();

/*Serial.print("min : "); Serial.print(min_temp);
Serial.print(" max : "); Serial.print(max_temp);
Serial.print(" curr : "); Serial.println(current_temp);*/

}//Get temp

//RGB LED COLOR
/////////////////////////////////////////////////////////////////
if((ct%RGB_FREQ)==0){update_led_color();}//Switch state? Update color?


//SEND INFO
/////////////////////////////////////////////////////////////////
if((ct%API_FREQ)==0){
  //SEND
  Serial.print("SENDING info through REST API");
  delay(2000);
  Serial.println("  Done!");
  }

  
//RECEIVE INFO
/////////////////////////////////////////////////////////////////
if(((ct+API_FREQ/2)%API_FREQ)==0){
  //RECEIVE
  Serial.print("RECEIVEING info through REST API");
  delay(2000);
  Serial.println("  Done!");
  }
 
   }

  //Communicate to server
  //noInterrupts();
  //send_to_api(current_temp);
  //get_from_api();
  //interrupts();
  //delay(2000);
 

  }



void update_acc(void){
  
  getAccel(); 
  //FOR THE SERIAL PLOTTER
  //Serial.println(phi);
  /*Serial.print(ax);Serial.print(" ");
  Serial.print(ay);Serial.print(" ");
  Serial.println(az);*/
  
  if(phi<SIP_THRESHOLD){
  sip_cycle_counter=0;
  } else if(phi>SIP_THRESHOLD){
  sip_cycle_counter+=1;
  
  if(sip_cycle_counter>=MIN_SIP_DUR){//ONE SIP!
      //CALL PRAVEERS API FOR ADDING A SIP
      
      sip_cycle_counter=0;
      tot_sips+=1;         
  }
  }  
  
}



void update_led_color(void){

   switch (rgb_state) {
  case 2:
    // Find my drink
    rainbow();
    break;
  case 1:
    // Need assistance
    alarm_rgb();
    break;
  default:
    // Light according to temp
    scale_temp(current_temp);
  }
  
}




// ACCEL FUNCTIONS
void getGyro(void)
{
accelgyro.getRotation(&gx, &gy, &gz);
/*
//FOR THE SERIAL PLOTTER
Serial.print(gx);
Serial.print(" ");
Serial.print(gy);
Serial.print(" ");
Serial.println(gz);
*/


/*
//TEXT FORMAT
Serial.print("gx: ");
Serial.print(gx);
Serial.print(" gy:");
Serial.print(gy);
Serial.print(" gz:");
Serial.print(gz);
*/

/*
 * BEFORE
Serial.print("gx:");
Serial.println(gx);
Serial.print("gy:");
Serial.println(gy);
Serial.print("gz:");
Serial.println(gz);*/

}

void getAccel(void)
{
accelgyro.getAcceleration(&ax, &ay, &az);
/*
ax = ax*(1-s_f1-s_f2)+ax_p1*s_f1+ax_p2*s_f2;
ay = ay*(1-s_f1-s_f2)+ay_p1*s_f1+ay_p2*s_f2;
az = az*(1-s_f1-s_f2)+az_p1*s_f1+az_p2*s_f2;*/

ax = ax*(1-s_f1)+ax_p1*s_f1;
ay = ay*(1-s_f1)+ay_p1*s_f1;
az = az*(1-s_f1)+az_p1*s_f1;

if(ax>MAX_ACC){ax=MAX_ACC;}
if(ay>MAX_ACC){ay=MAX_ACC;}
if(az>MAX_ACC){az=MAX_ACC;}

ar = sqrt(pow(ax,2)+pow(az,2));
atot=sqrt(pow(ar,2)+pow(ay,2));

if(atot<10000){atot=10000;}

phi=90*(1+ay/atot);

//if(phi>180){phi=phi;}

//FOR THE SERIAL PLOTTER
//Serial.println(phi);

//Update oldest value
/*
ax_p2=ax_p1;
ay_p2=ay_p1;
az_p2=az_p1;*/

//Update previous value
ax_p1=ax;
ay_p1=ay;
az_p1=az; 

/*
ax = ax*(1-s_f1-s_f2)+ax_p1*s_f1+ax_p1*s_f2;
ay = ay*(1-s_f1-s_f2)+ay_p1*s_f1+ay_p1*s_f2;
az = az*(1-s_f1-s_f2)+az_p1*s_f1+az_p1*s_f2;
*/

/*//FOR THE SERIAL PLOTTER
Serial.print(ay);
Serial.print(" ");
Serial.print(sqrt(pow(ax,2)+pow(az,2)));
Serial.print(" ");
Serial.println(sqrt(pow(ax,2)+pow(ay,2)+pow(az,2)));*/
}

