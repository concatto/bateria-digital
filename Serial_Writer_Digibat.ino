#include <Time.h>

const byte HEARTBEAT[] = {85, 78, 73};
const int pins[] = {A0, A1};

time_t tempo;

// the setup routine runs once when you press reset:
void setup() {
    tempo = now();
    Serial.begin(9600);
}

// the loop routine runs over and over again forever:
void loop() {
    if (second(now()) - second(tempo) > 1) {
        Serial.write(HEARTBEAT, 3);
        delay(20);
        Serial.flush();
        tempo = now();
    }
    for (int i = 0; i < 1; i++) {
        int forca = analogRead(pins[i]);
      
        int len = 3;
        byte data[len];
        message(i, forca, data);
        Serial.write(data, len);
        delay(10);
    }
}

void message(byte pin, int value, byte bytes[]) {
    bytes[0] = pin;
    bytes[1] = value & 0xFF;
    bytes[2] = (value >> 8) & 0xFF;
}
