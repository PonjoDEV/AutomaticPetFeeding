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
#include <SoftwareSerial.h>   // Biblioteca utilzada no HC05
#include <DS3231.h>           //Inclui a biblioteca do DS3231 Shield


// ======================================================================================================
// --- Mapeamento de Hardware modulo RTC ---
#define vcc 17
#define gnd 16

// --- Mapeamento de Hardware modulo HC05 ---
SoftwareSerial serialBT(2,3);
int valorBT;

// --- Mapeamento de Hardware modulo e ligações Arduino ---
#define relay 7

// ======================================================================================================
// --- Declaração de Objetos ---
DS3231 rtc(SDA, SCL);
Time t;
int horas, minutos, segundos, ano, mes, dia, nHorarios = 0, flag = 0, resetDia = 0, tempoAtivado =5000;
int vHoras[5];
int vMin[5];
int vQuant[5];

// ======================================================================================================
// --- Configurações Iniciais ---
void setup() {
  //Configuração dos pinos
  pinMode(relay, OUTPUT);
  pinMode(vcc, OUTPUT);
  pinMode(gnd, OUTPUT);
  digitalWrite(vcc, HIGH);
  digitalWrite(gnd, LOW);

  // --- Povoando os vetores, será feito através aplicativo android ---
  // --- Horas em que ocorre alimentação
  vHoras[0]= 5;
  vHoras[1]= 9;
  vHoras[2]= 13;
  vHoras[3]= 17;
  vHoras[4]= 21;
  // --- Minutos em que ocorre alimentação
  vMin[0]= 20;
  vMin[1]= 30;
  vMin[2]= 0;
  vMin[3]= 30;
  vMin[4]= 20;
  // --- Quantidad de vezes ao dia que ocorre alimentação ######################## NÃO ESTÁ SENDO UTILIZADO, PODE APAGAR ############################
  vQuant[0]= 1;
  vQuant[1]= 2;
  vQuant[2]= 3;
  vQuant[3]= 4;
  vQuant[4]= 5;


  serialBT.begin(9600); //inicializa comunicação serial do BT em 9600 baud rate
  Serial.begin(9600);   //Inicia comunicações Serial em 9600 baud rate

  rtc.begin();  //Inicializa RTC
  
  //Descomente as linhas a seguir para configurar o horário, após comente e faça o upload novamente para o Arduino
  //rtc.setDOW(SUNDAY);         // Set Day-of-Week to SUNDAY
  //rtc.setTime(11,37, 10);     // Set the time to 12:00:00 (24hr format)
  //rtc.setDate(03,12,2023);    // Set the date to (dd,mm,yyyy)

}  

// ======================================================================================================
// --- Loop Infinito ---
void loop() {

  //  O COMANDO A SER MANDADO PELO APP ANDROID É AO APERTAR O BOTÃO
  //  'Y' 
  //  DELAY(1000) 
  //  '0' 
  //  COMO DADO TRANSMITIDO

  //Inicializando com valor LOW para não mandar energia ao pino
  digitalWrite(relay, LOW);

  //Caso o dia mude, o contador do numero de alimentações deverá ser resetado
  if(resetDia != dia){
    nHorarios =0;
    resetDia =dia;
  }

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
  Serial.print(rtc.getDOWStr());  //Imprime o dia da semana
  Serial.print("  --  ");
  Serial.print(rtc.getDateStr());  
  Serial.print("  --  ");

  Serial.println(rtc.getTimeStr()); //Imprime o horário

  if (serialBT.available()>0){    //Checando se há dados chegando pela comunicação serial
    valorBT = serialBT.read();    //Lendo o byte mais antigo no buffer serial
    flag =0;                      //Caso tenha alguma nova leitura, a flag será definida para 0
  }
  if (valorBT != 'Y'){          //Caso botão de alimentar do App Android não esteja pressionado, seguir com alimentação padrão automatica
    for(int i=0; i<5;i++){      
      if(vHoras[i]==horas && vMin[i] == minutos && nHorarios<=i){        
        digitalWrite(relay, HIGH);
        delay(tempoAtivado);
        digitalWrite(relay, LOW);
        nHorarios++;
        Serial.println(nHorarios);
      }else{        
        digitalWrite(relay, LOW);
      }
    }    
    if (flag == 0){ 
      Serial.println("Alimentação no modo automático"); // Caso haja uma nova entrada do BT, será acusado a mudança através do serial, (Utilizar Serial ou serialBT ?)
      flag =1;
    }
  } else{                       
    digitalWrite(relay, HIGH);
    Serial.println("Alimentação no modo manual");   // Caso haja uma nova entrada do BT, será acusado a mudança através do serial, (Utilizar Serial ou serialBT ?)
    flag =1;
    delay(tempoAtivado);
    digitalWrite(relay, LOW);
  }
  //Atualiza monitor a cada segundo
  delay(1000);
}