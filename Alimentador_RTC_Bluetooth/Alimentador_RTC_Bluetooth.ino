/* ======================================================================================================
   
   Usina Info & WR Kits
   
   RTC DS3231


   Hardware
            DS3231    -> Arduino UNO
            -----------------------------------------
            VCC pin   -> Analógico 3 (utilizado como digital 17)
            GND pin   -> Analógico 3 (utilizado como digital 16)
            SDA pin   -> Analógico 4 (SDA)  
            SCL pin   -> Analógico 5 (SCL)  
            -----------------------------------------
    
   Autor: Eng. Wagner Rambo  Data: Novembro de 2016
   
   www.wrkits.com.br | facebook.com/wrkits | youtube.com/user/canalwrkits
   
====================================================================================================== */


// ======================================================================================================
// --- Bibliotecas Auxiliares ---
//#include <SoftwareSerial.h> // Biblioteca utilzada no HC05
#include <DS3231.h>           //Inclui a biblioteca do DS3231 Shield


// ======================================================================================================
// --- Mapeamento de Hardware modulo RTC ---
#define vcc 17
#define gnd 16
// --- Mapeamento de Hardware modulo HC05 ---
#define ledPin 9
#define buttonPin 8

// ======================================================================================================
// --- Declaração de Objetos ---
DS3231 rtc(SDA, SCL);
Time t;
int horas, minutos, segundos, ano, mes, dia, nHorarios = 0;


// ======================================================================================================
// --- Configurações Iniciais ---
void setup() {
  //Configuração dos pinos
  pinMode(7, OUTPUT);
  pinMode(vcc, OUTPUT);
  pinMode(gnd, OUTPUT);
  digitalWrite(vcc, HIGH);
  digitalWrite(gnd, LOW);

  Serial.begin(9600);  //Inicia comunicações Serial em 9600 baud rate

  rtc.begin();  //Inicializa RTC
  
  //Descomente as linhas a seguir para configurar o horário, após comente e faça o upload novamente para o Arduino
  //rtc.setDOW(SUNDAY);         // Set Day-of-Week to SUNDAY
  //rtc.setTime(11,37, 10);     // Set the time to 12:00:00 (24hr format)
  //rtc.setDate(03,12,2023);    // Set the date to (dd,mm,yyyy)

}  //end setup


// ======================================================================================================
// --- Loop Infinito ---
void loop() {

  t = rtc.getTime();
  horas = t.hour;
  minutos = t.min;
  segundos = t.sec;
  mes = t.mon;
  ano = t.year;
  dia = t.date;

  //Serial.print(horas);
  //Serial.print("   ");
  Serial.print(dia);
  Serial.print("  --  ");
  //Imprime o dia da semana
  Serial.print(rtc.getDOWStr());
  Serial.print("  --  ");
  Serial.print(rtc.getDateStr());  
  Serial.print("  --  ");

  //Imprime o horário
  Serial.println(rtc.getTimeStr());
  //if (!buttonPressed){ //caso botão bluetooth esteja apertado, ele pulará a etapa de alimentação marcada, necessário implementar o buttonPressed
    if (segundos % 5 == 0 && segundos % 10 != 0) {
      digitalWrite(7, HIGH);
    } else {
      if (segundos % 10 == 0) {
        digitalWrite(7, LOW);
      }
    }
  //} else{
  //  digitalWrite(7, HIGH);
  //}
  //Atualiza monitor a cada segundo
  delay(1000);

}  //end loop