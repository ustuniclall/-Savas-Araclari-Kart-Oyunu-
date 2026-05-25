package oyun;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;


// Savaş Aracı (Abstract)
abstract class Savas_araci {
    int seviye_puani = 0;
    int dayaniklilik;
    int vurus;
    String sinif;
    boolean kullanildiMi;

    public Savas_araci(int dayaniklilik, int vurus, String sinif, boolean kullanildiMi) {
        this.dayaniklilik = dayaniklilik;
        this.vurus = vurus;
        this.sinif = sinif;
        this.kullanildiMi = kullanildiMi;
    }
    
    public abstract int vurusDegeriHesapla(Savas_araci rakip);
    
    public String toString(){
        return "Savas_araci {" +
                "sinif_adi=" + sinif + '\'' +
                ", dayaniklilik=" + dayaniklilik +
                ", vurma_gucu=" + vurus +
                ", seviye puani=" + seviye_puani +
                ", kullanildiMi=" + kullanildiMi +
                '}';
    }
}

// Hava Araçları (Abstract)
abstract class Hava_araci extends Savas_araci {
    public Hava_araci(int dayaniklilik, int vurus, String sinif, boolean kullanildiMi) {
        super(dayaniklilik, vurus, sinif, kullanildiMi);
    }

    abstract int kara_vurus_avantaji();
}

// Kara Araçları (Abstract)
abstract class Kara_araci extends Savas_araci {
    public Kara_araci(int dayaniklilik, int vurus, String sinif, boolean kullanildiMi) {
        super(dayaniklilik, vurus, sinif, kullanildiMi);
    }

    abstract int deniz_vurus_avantaji();
}

// Deniz Araçları (Abstract)
abstract class Deniz_araci extends Savas_araci {
    public Deniz_araci(int dayaniklilik, int vurus, String sinif, boolean kullanildiMi) {
        super(dayaniklilik, vurus, sinif, kullanildiMi);
    }

    abstract int hava_vurus_avantaji();
}

// Uçak sınıfı
class Ucak extends Hava_araci {
    public Ucak() {
        super(20, 10, "Ucak", false);
    }

    @Override
    int kara_vurus_avantaji() {
        return 10;
    }
    
    @Override
    public int vurusDegeriHesapla(Savas_araci rakip){
        if(rakip instanceof Kara_araci)
            return vurus + kara_vurus_avantaji();

        return vurus;
    }
}

// Siha sınıfı
class Siha extends Hava_araci {
    public Siha() {
        super(15, 10, "Siha", false);
    }

    @Override
    int kara_vurus_avantaji() {
        return 10;
    }

    int deniz_vurus_avantaji(){
        return 10;
    }
    
    @Override
    public int vurusDegeriHesapla(Savas_araci rakip){
        if(rakip instanceof Kara_araci)
            return vurus + kara_vurus_avantaji();
        else if (rakip instanceof Deniz_araci)
            return vurus + deniz_vurus_avantaji();
        
        return vurus;       
    }
}

// Obüs sınıfı
class Obus extends Kara_araci {
    public Obus() {
        super(20, 10, "Obus", false);
    }

    @Override
    int deniz_vurus_avantaji() {
        return 5;
    }
    
    @Override
    public int vurusDegeriHesapla(Savas_araci rakip){
       if (rakip instanceof Deniz_araci)
            return vurus + deniz_vurus_avantaji();
        
        return vurus;   
    }
}

// KFS sınıfı
class KFS extends Kara_araci {
    public KFS() {
        super(10, 10, "KFS", false);
    }

    @Override
    int deniz_vurus_avantaji() {
        return 10;
    }

    int hava_vurus_avantaji() {
        return 20;
    }
    
    @Override
    public int vurusDegeriHesapla(Savas_araci rakip){
        if(rakip instanceof Deniz_araci)
            return vurus + deniz_vurus_avantaji();
        else if (rakip instanceof Hava_araci)
            return vurus + hava_vurus_avantaji();
        
        return vurus;     
    }
}

// Firkateyn sınıfı
class Firkateyn extends Deniz_araci {
    public Firkateyn() {
        super(25, 10, "Firkateyn", false);
    }

    @Override
    int hava_vurus_avantaji() {
        return 5;
    }
    
    @Override
    public int vurusDegeriHesapla(Savas_araci rakip){
        if(rakip instanceof Hava_araci)
            return vurus + hava_vurus_avantaji();
        
        return vurus;        
    }
}

// Sida sınıfı
class Sida extends Deniz_araci {
    public Sida() {
        super(15, 10, "Sida", false);
    }

    @Override
    int hava_vurus_avantaji() {
        return 10;
    }

    int kara_vurus_avantaji() {
        return 10;
    }
    
    @Override
    public int vurusDegeriHesapla(Savas_araci rakip){
        if(rakip instanceof Hava_araci)
            return vurus + hava_vurus_avantaji();
        else if (rakip instanceof Kara_araci)
            return vurus + kara_vurus_avantaji();
        
        return vurus;       
    }
}

//seçme işlemi kurallaeı geçerli değil!!!!
class Oyuncu {
    private int oyuncu_ID;
    private String oyuncu_adi;
    private int skor;
    private List<String> kart_listesi;

    // Parametresiz yapıcı metod
    public Oyuncu() {
        this.oyuncu_ID = 0;
        this.oyuncu_adi = "bilgisayar";
        this.skor = 0;
        this.kart_listesi = new ArrayList<>();
    }

    // Parametreli yapıcı metod
    public Oyuncu(int oyuncu_ID, String oyuncu_adi, int skor) {
        this.oyuncu_ID = oyuncu_ID;
        this.oyuncu_adi = oyuncu_adi;
        this.skor = skor;
        this.kart_listesi = new ArrayList<>();
    }

    // Skor gösterme metodu
    public void Skor_goster() {
        System.out.println(oyuncu_adi + " Skoru: " + skor);
    }

    // Kart seçme fonksiyonu (Kullanıcı ve bilgisayar için farklı çalışacak)
    public void kart_sec(boolean Bilgisayar) {
        if (Bilgisayar) {
            // Bilgisayarın kart seçmesi
            Random rand = new Random();
            int index = rand.nextInt(kart_listesi.size());
            System.out.println(oyuncu_adi + " Kart Secti: " + kart_listesi.get(index));
            kart_listesi.remove(index);  // Kartı kullandı
        } else {
            // Kullanıcının kart seçmesi
            Scanner scanner = new Scanner(System.in);
            System.out.println(oyuncu_adi + " elindeki kartlar: " + kart_listesi);
            System.out.print("Bir kart secin: ");
            int index = scanner.nextInt() - 1; // Kullanıcı kartı numara ile seçer
            if (index >= 0 && index < kart_listesi.size()) {
                System.out.println(oyuncu_adi + " Kart Secti: " + kart_listesi.get(index));
                kart_listesi.remove(index);  // Kartı kullandı
            } else {
                System.out.println("Geçersiz kart numarası.");
            }
        }
    }

    // Kart listesine kart eklemek için yardımcı fonksiyon
    public void kart_ekle(String kart) {
        kart_listesi.add(kart);
    }

    // Kart listesine kartları almak
    public List<String> kart_listesi_kart_al() {
        return kart_listesi;
    }

    // Skor artırma
    public void skor_arttir(int deger) {
        skor += deger;
    }

    // Oyuncu adı döndürme
    public String oyuncu_adi_dondur() {
        return oyuncu_adi;
    }
    
    public int getSkor(){
        return skor;
    }
}

public class Oyun extends Application {

    private int seviyePuani = 0; // Varsayılan seviye puanı
    private int hamleSayisi = 0;
    private int maksimumHamle = 10; // Varsayılan maksimum hamle sayısı
    private String kullaniciAdi;
    private List<Savas_araci> tumKartlar;
    private List<Savas_araci> uygunKartlar;
    private List<Savas_araci> kullanici_kartlari;
    private List<Savas_araci> bilgisayar_kartlari;
    private List<Savas_araci> bilgisayar_secimleri;
    private List<Savas_araci> kullanici_secimleri;
    
    private boolean ilkdegerler = true; //program başlarken oyuncu ve bilgisayar kartlarını ayarlamak için
    
    private boolean oyunBitti = false;
    
    Oyuncu oyuncu = new Oyuncu(1, "Oyuncu", 0);
    Oyuncu bilgisayar = new Oyuncu(1, "Bilgisayar", 0);
    
    private Stage ilkAsama;
        
    @Override
    public void start(Stage ilk) {

                ilkAsama = ilk;
        
        try (FileWriter writer = new FileWriter("dosya.txt", false)) { // false ile eski içeriği sıfırla
            // Dosya içeriğini temizlemek için hiçbir şey yazmadan kapat
            System.out.println("Dosyanin icerigi basariyla silindi.");
        } catch (IOException e) {
            System.out.println("Bir hata oluştu: " + e.getMessage());
        }
           
        // Kullanıcı adı sahnesi       
       Label isimEtiketi = new Label("Adınızı girin:");

        TextField isimAlani = new TextField();
        isimAlani.setPromptText("Adınızı buraya yazın");
        isimAlani.setMaxWidth(300);

        Button baslamaButonu = new Button("Başla");
        baslamaButonu.setOnAction(e -> {
            kullaniciAdi = isimAlani.getText();
            if (!kullaniciAdi.isEmpty()) {
                System.out.println("Hos geldiniz, " + kullaniciAdi + "!");
                seviyePuaniDegistirme(ilkAsama, kullaniciAdi);
                
                dosyayaYaz("Hos geldiniz, " + kullaniciAdi + "!");
            } else {
                System.out.println("Lütfen adınızı giriniz.");
                dosyayaYaz("Lütfen adınızı giriniz.");
            }
        });

        isimAlani.setOnKeyPressed(e-> {
            if(e.getCode() == KeyCode.ENTER)
                baslamaButonu.fire();
        });
        
        // Arka plan resmi
        Image arkaPlanResmi = new Image("file:arkaplanresmi.jpg");
        BackgroundImage arkaPlan = new BackgroundImage(
            arkaPlanResmi,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        // Ana düzen
        VBox vbox = new VBox(10, isimEtiketi, isimAlani, baslamaButonu);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-alignment: center;");
        vbox.setBackground(new Background(arkaPlan)); // Arka planı ayarla

        Scene kullaniciEkrani = new Scene(vbox, 800, 450);

        ilk.setTitle("Kart Oyunu");
        ilk.setScene(kullaniciEkrani);
        ilk.show();

    }

    private void seviyePuaniDegistirme(Stage ilkAsama, String kullaniciAdi) {
        // Seviye değiştirme ekranı
        Label seviyeEkrani = new Label("Seviye puanını değiştirmek istiyor musunuz?\nEvet ise 1'e basın.");
        TextField cevapAlani = new TextField();
        cevapAlani.setPromptText("Cevabınızı girin (1 veya başka bir şey)");
        cevapAlani.setMaxWidth(300);
        
        // Arka plan resmi
        Image arkaPlanResmi = new Image("file:arkaplanresmi.jpg");
        BackgroundImage arkaPlan = new BackgroundImage(
            arkaPlanResmi,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );

        Button devamButonu = new Button("Devam");
        devamButonu.setOnAction(e -> {
            String cevap = cevapAlani.getText();
            if ("1".equals(cevap)) {
                seviyePuaniDegistirmeGirdi(ilkAsama, kullaniciAdi);
            } else {
                System.out.println("Varsayilan seviye puani: " + seviyePuani);
                maksimumHamleDegistirme(ilkAsama, kullaniciAdi);
                
                dosyayaYaz("Varsayilan seviye puani: " + seviyePuani);
            }
        });
        
        cevapAlani.setOnKeyPressed(e-> {
                if(e.getCode() == KeyCode.ENTER)
                    devamButonu.fire();
        });

        VBox vbox = new VBox(10, seviyeEkrani, cevapAlani, devamButonu);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
        vbox.setBackground(new Background(arkaPlan)); // Arka planı ayarla
        
        Scene seviyePuaniEkrani = new Scene(vbox, 800, 450);
        ilkAsama.setScene(seviyePuaniEkrani);
    }

    private void seviyePuaniDegistirmeGirdi(Stage ilkAsama, String kullaniciAdi) {
        // Yeni seviye puanı girişi ekranı
        Label seviyeEkrani = new Label("Yeni seviye puanınızı girin:");
        TextField seviyeGiris = new TextField();
        seviyeGiris.setPromptText("Seviye puanı girin");
        seviyeGiris.setMaxWidth(300);
        
        Button onaylamaButonu = new Button("Onayla");
        onaylamaButonu.setOnAction(e -> {
            try {
                seviyePuani = Integer.parseInt(seviyeGiris.getText());
                System.out.println("Seviye puani degistirildi: " + seviyePuani);
                maksimumHamleDegistirme(ilkAsama, kullaniciAdi);
                
                dosyayaYaz("Seviye puani degistirildi: " + seviyePuani);
            } catch (NumberFormatException ex) {
                Alert uyari = new Alert(AlertType.ERROR);
                uyari.setTitle("Hata");
                uyari.setHeaderText(null);
                uyari.setContentText("Lütfen geçerli bir sayı girin.");
                uyari.showAndWait();
            }
        });
        
        // Arka plan resmi
        Image arkaPlanResmi = new Image("file:arkaplanresmi.jpg");
        BackgroundImage arkaPlan = new BackgroundImage(
            arkaPlanResmi,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        
        seviyeGiris.setOnKeyPressed(e-> {
            if(e.getCode() == KeyCode.ENTER)
                onaylamaButonu.fire();
        });

        VBox vbox = new VBox(10, seviyeEkrani, seviyeGiris, onaylamaButonu);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
        vbox.setBackground(new Background(arkaPlan)); // Arka planı ayarla        
        Scene seviyePuaniEkrani = new Scene(vbox, 800, 450);
        ilkAsama.setScene(seviyePuaniEkrani);
    }

    private void maksimumHamleDegistirme(Stage ilkAsama, String kullaniciAdi) {
        // Maksimum hamle değiştirme ekranı
        Label hamleEkrani = new Label("Maksimum hamle sayısını değiştirmek istiyor musunuz?\nEvet ise 1'e basın.");
        TextField cevapAlani = new TextField();
        cevapAlani.setPromptText("Cevabınızı girin (1 veya başka bir şey)");
        cevapAlani.setMaxWidth(300);

        Button devamButonu = new Button("Devam");
        devamButonu.setOnAction(e -> {
            String cevap = cevapAlani.getText();
            if ("1".equals(cevap)) {
                maksimumHamleDegistirmeGiris(ilkAsama, kullaniciAdi);
            } else {
                System.out.println("Varsayilan maksimum hamle sayisi: " + maksimumHamle);
                kartSecimSahnesi(ilkAsama, kullaniciAdi);
                
                dosyayaYaz("Varsayilan maksimum hamle sayisi: " + maksimumHamle);
            }
        });
        
        cevapAlani.setOnKeyPressed(e-> {
                if(e.getCode() == KeyCode.ENTER)
                    devamButonu.fire();
        });
        
        // Arka plan resmi
        Image arkaPlanResmi = new Image("file:arkaplanresmi.jpg");
        BackgroundImage arkaPlan = new BackgroundImage(
            arkaPlanResmi,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        
        VBox vbox = new VBox(10, hamleEkrani, cevapAlani, devamButonu);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
        vbox.setBackground(new Background(arkaPlan)); // Arka planı ayarla         
        Scene hamleSayisiEkrani = new Scene(vbox, 800, 450);
        ilkAsama.setScene(hamleSayisiEkrani);
    }

    private void maksimumHamleDegistirmeGiris(Stage ilkAsama, String kullaniciAdi) {
        // Yeni maksimum hamle sayısı girişi ekranı
        Label hamleEkrani = new Label("Yeni maksimum hamle sayısını girin:");
        TextField hamleGiris = new TextField();
        hamleGiris.setPromptText("Hamle sayısını girin");
        hamleGiris.setMaxWidth(300);

        Button onaylaButonu = new Button("Onayla");
        onaylaButonu.setOnAction(e -> {
            try {
                maksimumHamle = Integer.parseInt(hamleGiris.getText());
                System.out.println("Maksimum hamle sayisi degistirildi: " + maksimumHamle);
                kartSecimSahnesi(ilkAsama, kullaniciAdi);
                
                dosyayaYaz("Maksimum hamle sayisi degistirildi: " + maksimumHamle);
            } catch (NumberFormatException ex) {
                Alert uyari = new Alert(AlertType.ERROR);
                uyari.setTitle("Hata");
                uyari.setHeaderText(null);
                uyari.setContentText("Lütfen geçerli bir sayı girin.");
                uyari.showAndWait();
            }
        });
        
         // Arka plan resmi
        Image arkaPlanResmi = new Image("file:arkaplanresmi.jpg");
        BackgroundImage arkaPlan = new BackgroundImage(
            arkaPlanResmi,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        
        hamleGiris.setOnKeyPressed(e-> {
            if(e.getCode() == KeyCode.ENTER)
                onaylaButonu.fire();
        });

        VBox vbox = new VBox(10, hamleEkrani, hamleGiris, onaylaButonu);
        vbox.setStyle("-fx-padding: 20; -fx-alignment: center;");
        vbox.setBackground(new Background(arkaPlan)); // Arka planı ayarla         
        Scene hamleSayisiEkrani = new Scene(vbox, 800, 450);
        ilkAsama.setScene(hamleSayisiEkrani);
    }

    private void kartSecimSahnesi(Stage ilkAsama, String kullaniciAdi) {
                
        Label kullanici = new Label(kullaniciAdi);
        kullanici.setStyle("-fx-font: normal bold 14px 'system'");
        
        // Kart seçimi sahnesi
        Label talimatEkrani = new Label("3 kart seçmelisiniz:");

        // Seçilen kartların tutulduğu liste
        List<String> secilenKartlar = new ArrayList<>();
        
        if(ilkdegerler){
            ilkDegerleriOlustur();
            ilkdegerler = false;
        }
        
        kullanici_secimleri = new ArrayList<>();
        
        // GridPane ile düzen
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // uyguna kartlar buton olarak ekleme
        for (int i = 1; i <= kullanici_kartlari.size(); i++) {
            
            String kullanildiMi = kullanici_kartlari.get(i-1).kullanildiMi ? "" :  "\nKullanılmadı";

           Button butonKullanici = new Button("Kullanıcı\nKart " + i + "\n" + kullanici_kartlari.get(i-1).sinif +
                    "\nDayanıklık: " + kullanici_kartlari.get(i-1).dayaniklilik + 
                    "\nVuruş: " + kullanici_kartlari.get(i-1).vurus +
                    kullanildiMi);
                butonKullanici.setMinSize(100, 150);
                butonKullanici.setStyle("-fx-background-color: lightyellow;"); // Açık sarı renk
                butonKullanici.setUserData(i);

            
            butonKullanici.setOnAction(e -> {
                int secilenKart = (int) butonKullanici.getUserData();
                
                if (secilenKartlar.contains(butonKullanici.getText())) {
                    secilenKartlar.remove(butonKullanici.getText());
                    butonKullanici.setStyle(""); // Varsayılan stile döndür
                    
                    kullanici_kartlari.get(secilenKart-1).kullanildiMi = false;
                } else if (secilenKartlar.size() < 3) {
                    if(kullanilmamisKartVarMi()){
                        if (!kullanici_kartlari.get(secilenKart - 1).kullanildiMi) {
                            // Kullanılmamış kart seçildiğinde işlemi gerçekleştir
                            secilenKartlar.add(butonKullanici.getText());
                            kullanici_kartlari.get(secilenKart - 1).kullanildiMi = true;
                            kullanici_secimleri.add(kullanici_kartlari.get(secilenKart - 1));
                            butonKullanici.setStyle("-fx-background-color: lightgreen;");
                            
                            if(secilenKartlar.size() == 3){
                                // Seçilen kartları yazdır
                                System.out.println("\nKullanici Sectigi Kartlar:");
                                for (Savas_araci kart : kullanici_secimleri) {
                                    System.out.println(kart);
                                }
                            }
                        } else {
                            showAlert("Lütfen kullanılmamış kartlardan birini seçin!");
                        }
                    } else {
                        secilenKartlar.add(butonKullanici.getText());
                        kullanici_kartlari.get(secilenKart-1).kullanildiMi = true;
                        kullanici_secimleri.add(kullanici_kartlari.get(secilenKart-1));
                        butonKullanici.setStyle("-fx-background-color: lightgreen;"); // Seçim vurgusu
                        
                        if(secilenKartlar.size() == 3){
                            // Seçilen kartları yazdır
                            System.out.println("\nKullanici Sectigi Kartlar:");
                            dosyayaYaz("Kullanici Sectigi Kartlar:");
                            for (Savas_araci kart : kullanici_secimleri) {
                                System.out.println(kart);
                                dosyayaYaz(kart.toString());
                            }
                            
                        }
                       
                    }
                    
                } else {
                    Alert uyari = new Alert(AlertType.INFORMATION);
                    uyari.setTitle("Uyarı");
                    uyari.setHeaderText(null);
                    uyari.setContentText("Tam 3 kart seçebilirsiniz. Fazla kart seçemezsiniz.");
                    uyari.showAndWait();
                }
                
            
            });

            int column = (i-1) % 3;
            int row = (i-1) / 3;
            gridPane.add(butonKullanici, column, row); // 3 sütunlu düzen
        }
        
        // GridPane ile düzen
        GridPane gridPane2 = new GridPane();
        gridPane2.setHgap(10);
        gridPane2.setVgap(10);
        gridPane2.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        // uyguna kartlar buton olarak ekleme
        for (int i = 1; i <= bilgisayar_kartlari.size(); i++) {
            String kullanildiMi = bilgisayar_kartlari.get(i-1).kullanildiMi ? "" :  "\nKullanilmadı";
            
            Button butonBilgisayar = new Button("Bilgisayar\nKart " + i + "\n" + bilgisayar_kartlari.get(i-1).sinif +
                    "\nDayanıklık: " + bilgisayar_kartlari.get(i-1).dayaniklilik + 
                    "\nVuruş: " + bilgisayar_kartlari.get(i-1).vurus +
                    kullanildiMi);
                butonBilgisayar.setMinSize(100, 150);
                butonBilgisayar.setStyle("-fx-background-color: lightblue;"); // Açık sarı renk
                butonBilgisayar.setUserData(bilgisayar_kartlari.get(i-1));

            
            int column = (i-1) % 3;
            int row = (i-1) / 3;
            gridPane2.add(butonBilgisayar, column, row); // 3 sütunlu düzen
        }
        
        Button cikisButonu = new Button("Çıkış");
        cikisButonu.setMaxWidth(150);
        cikisButonu.setOnAction(e -> ilkAsama.close());
       
        Button onaylaButonu = new Button("Kartları Karşılaştır");
        onaylaButonu.setMaxWidth(150);
        onaylaButonu.setOnAction(e -> {
            if (secilenKartlar.size() < 3) {
                Alert uyari = new Alert(AlertType.WARNING);
                uyari.setTitle("Uyarı");
                uyari.setHeaderText(null);
                uyari.setContentText("Lütfen 3 kart seçin.");
                uyari.showAndWait();
            } else {
                hamleYap();
            }
        });

        onaylaButonu.setOnKeyPressed(e-> {
            if(e.getCode() == KeyCode.ENTER)
                onaylaButonu.fire();
        });
        
        Label hamle = new Label("Hamle Sayısı: " + hamleSayisi );
        hamle.setStyle("-fx-font:normal bold 14px 'system'; -fx-text-fill: black");
        Label maxHamle = new Label("Maximum Hamle Sayısı: " + maksimumHamle );
        maxHamle.setStyle("-fx-font:normal bold 14px 'system'; -fx-text-fill: black");
        
        Label oyuncuSkoru = new Label("Oyuncu Skoru: " + oyuncu.getSkor() );
        oyuncuSkoru.setStyle("-fx-font:normal bold 14px 'system'; -fx-text-fill: black");
        Label bilgisayarSkoru = new Label("Bilgisayar Skoru: " + bilgisayar.getSkor() );
        bilgisayarSkoru.setStyle("-fx-font:normal bold 14px 'system'; -fx-text-fill: black");
        
        HBox mainLayout = new HBox(gridPane, gridPane2);

        VBox bilgiAlani1 = new VBox(10, hamle, maxHamle);  // 10px arayla yerleştir
        bilgiAlani1.setAlignment(Pos.CENTER_LEFT);  // Bilgi alanlarını sola hizala

        VBox bilgiAlani2 = new VBox(10, oyuncuSkoru, bilgisayarSkoru);  // 10px arayla yerleştir
        bilgiAlani2.setAlignment(Pos.CENTER_RIGHT); // Bilgi alanlarını sağa hizala

        HBox mainLayout2 = new HBox(20, bilgiAlani2, bilgiAlani1);  // İki bilgi alanını 20px arayla yerleştir
        mainLayout2.setAlignment(Pos.CENTER);  // mainLayout2'yi ortada hizala

        VBox vbox = new VBox(20, kullanici, talimatEkrani, mainLayout2, mainLayout , oyunBitti?cikisButonu:onaylaButonu);
        vbox.setStyle("-fx-alignment: center; -fx-background-color: lightpink;"); // Arka plan rengi açık mor

        Scene kartEkrani = new Scene(vbox, 700, 750);
        ilkAsama.setScene(kartEkrani);

    }

    public static void main(String[] args) {
        launch(args);
  
    }
    
    public List<Savas_araci> rastgele_kart_sec(List<Savas_araci> kartlar, int kart_sayisi){

        List<Savas_araci> secilen_kartlar= new ArrayList<>();
        Random random = new Random();
        int kartlarSize = kartlar.size();

        for(int i=0;i<kart_sayisi;i++){
           int rastgele_index= random.nextInt(kartlarSize);

           if(kartlar.get(rastgele_index) instanceof Ucak)
               secilen_kartlar.add(new Ucak()); //her seferinde farklı uçak nesnesi oluştur
           else if(kartlar.get(rastgele_index) instanceof Siha)
               secilen_kartlar.add(new Siha());
           else if(kartlar.get(rastgele_index) instanceof Sida)
                secilen_kartlar.add(new Sida());
           else if(kartlar.get(rastgele_index) instanceof Obus)
                secilen_kartlar.add(new Obus());
           else if(kartlar.get(rastgele_index) instanceof KFS)
                secilen_kartlar.add(new KFS());
           else if(kartlar.get(rastgele_index) instanceof Firkateyn)
                secilen_kartlar.add(new Firkateyn());
        }
        return secilen_kartlar;
    }

    public List<Savas_araci> rastgeleYeniKartEkle(List<Savas_araci> uygunKartlar, List<Savas_araci> oyuncuKartlari){

        List<Savas_araci> secilen_kartlar= new ArrayList<>();
        Random random = new Random();

        int rastgele_index= random.nextInt(uygunKartlar.size());

        if(uygunKartlar.get(rastgele_index) instanceof Ucak)
            oyuncuKartlari.add(new Ucak()); //her seferinde farklı uçak nesnesi oluştur
        else if(uygunKartlar.get(rastgele_index) instanceof Siha)
            oyuncuKartlari.add(new Siha());
        else if(uygunKartlar.get(rastgele_index) instanceof Sida)
            oyuncuKartlari.add(new Sida());
        else if(uygunKartlar.get(rastgele_index) instanceof Obus)
            oyuncuKartlari.add(new Obus());
        else if(uygunKartlar.get(rastgele_index) instanceof KFS)
            oyuncuKartlari.add(new KFS());
        else if(uygunKartlar.get(rastgele_index) instanceof Firkateyn)
            oyuncuKartlari.add(new Firkateyn());

        return secilen_kartlar;
    }

    public List<Savas_araci> tumKartlariEkle(){
      // Kart listesini oluştur
      List<Savas_araci> tumKartlar = new ArrayList<>();
      tumKartlar.add(new Ucak());
      tumKartlar.add(new Firkateyn());
      tumKartlar.add(new Obus());
      tumKartlar.add(new KFS());
      tumKartlar.add(new Sida());
      tumKartlar.add(new Siha());

      Collections.shuffle(tumKartlar);
      return tumKartlar;
    }

    public List<Savas_araci> uygunKartlariGetir(List<Savas_araci> tumKartlar,int seviyePuani){

      List<Savas_araci> uygunKartlar;

      if (seviyePuani < 20) {
          uygunKartlar = new ArrayList<>();
          for (Savas_araci kart : tumKartlar) {
              if (Ucak.class.isInstance(kart) || Firkateyn.class.isInstance(kart) || Obus.class.isInstance(kart)) {
                  uygunKartlar.add(kart);
              }
          }
      } else {
          uygunKartlar = tumKartlar;
      }
      return uygunKartlar;
    }

    public void yazdir(List<Savas_araci> kartlar){

      for (Savas_araci kart : kartlar) {
          System.out.println(kart);
          dosyayaYaz(kart.toString());
      }

    }

    public int skorHesapla(List<Savas_araci> kartlar){
      int skor=0;

      for (Savas_araci kart : kartlar) {
        skor += kart.seviye_puani; 
      }

      return skor;
    }

    public int dayaniklilikHesapla(List<Savas_araci> kartlar){
      int skor=0;

      for (Savas_araci kart : kartlar) {
        skor += kart.dayaniklilik; 
      }

      return skor;
    }
    
    public void bilgisayarKartlariniSec (){
        
        // Bilgisayarın kartları rastgele seçmesi
        bilgisayar_secimleri = new ArrayList<>();
        
        Random random = new Random();
        int kartSayisi = bilgisayar_kartlari.size();

        while (bilgisayar_secimleri.size() < 3) {

            //Kullanilmamis kartlari listele
            List<Savas_araci> kullanilmamisKartlar = new ArrayList<>();
            for(Savas_araci kart : bilgisayar_kartlari){
                if(!kart.kullanildiMi)
                    kullanilmamisKartlar.add(kart);
            }

            Savas_araci secilenKart;
            //Eger kullanilmamis kart varsa onlardan rastgele sec
            if(!kullanilmamisKartlar.isEmpty()){
                secilenKart = kullanilmamisKartlar.get(random.nextInt(kullanilmamisKartlar.size()));
            } else {
                // Kullanilmamis kart kalmadi ise tum kartlardan rastgele sec
                secilenKart = bilgisayar_kartlari.get(random.nextInt(kartSayisi));
            }

            // Daha once secilmis kartlari eklememe kontrolu
            if (!bilgisayar_secimleri.contains(secilenKart)) {
                secilenKart.kullanildiMi = true;
                bilgisayar_secimleri.add(secilenKart);
                //System.out.println("Secilen kart: " + secilenKart.getClass().getSimpleName());
            }
        }
        
        System.out.println("\nBilgisayar Sectigi Kartlar:");
        dosyayaYaz("Bilgisayar Sectigi Kartlar:");
        for (Savas_araci kart : bilgisayar_secimleri) {
            System.out.println(kart);
            dosyayaYaz(kart.toString());
        }
    }

    public void hamleYap(){

        //max_Hamle sayısı kadar Hamle yap
        if (hamleSayisi < maksimumHamle ){
            
            hamleSayisi++;
            
            //Bilgisayar akrtlari rastgele secilir
            bilgisayarKartlariniSec();
            
            // kartları karşılaştır
            kartlariKarsilastir();

            // ilk hamleden sonra oyuncu kartlarına yeni kart ekle
            if(hamleSayisi > 1 && !oyunBitti){

                uygunKartlar = uygunKartlariGetir(tumKartlar, seviyePuani);

                rastgeleYeniKartEkle(uygunKartlar, kullanici_kartlari);
                rastgeleYeniKartEkle(uygunKartlar, bilgisayar_kartlari);

                //oyunu kartı 1 veya 2ye düşürse tek seferliğine yeni bir kart ekle
                if(kullanici_kartlari.size() == 2){
                    rastgeleYeniKartEkle(uygunKartlar, kullanici_kartlari);
                    oyunBitti = true; // oyunun tamamlanması için
                }

                if(bilgisayar_kartlari.size() == 2){
                    rastgeleYeniKartEkle(uygunKartlar, bilgisayar_kartlari);
                    oyunBitti = true; // oyunun tamamlanması içinß
                }

                System.out.println("\nYeni kart eklendikten sonra Oyuncu");
                dosyayaYaz("Yeni kart eklendikten sonra Oyuncu");
                yazdir(kullanici_kartlari);
                
                System.out.println("\nYeni kart eklendikten sonra Bilgisayar");
                dosyayaYaz("Yeni kart eklendikten sonra Bilgisayar");
                yazdir(bilgisayar_kartlari);
                
            }
     
            kullanici_secimleri.clear();
            
            kartSecimSahnesi(ilkAsama, kullaniciAdi);
            
        }  
    }

    public void kartlariKarsilastir(){
        
        //System.out.println("Kartlari karsilastir\n");        
        for(int i=0; i<3; i++){
           Savas_araci kart1 = kullanici_secimleri.get(i);
           Savas_araci kart2 = bilgisayar_secimleri.get(i);

           int bilgisayarYeniDayaniklilik =  kart2.dayaniklilik - kart1.vurusDegeriHesapla(kart2);
           bilgisayar_secimleri.get(i).dayaniklilik = bilgisayarYeniDayaniklilik;

           int oyuncuYeniDayaniklilik = kart1.dayaniklilik - kart2.vurusDegeriHesapla(kart1);
           kullanici_secimleri.get(i).dayaniklilik = oyuncuYeniDayaniklilik;

           if(bilgisayarYeniDayaniklilik <= 0){ // bilgisayar kartı elendi
               bilgisayar_kartlari.remove(kart2);
               if(kart2.seviye_puani < 10){
                   kullanici_secimleri.get(i).seviye_puani += 10;
                   oyuncu.skor_arttir(10);
               }
               else{
                   kullanici_secimleri.get(i).seviye_puani += kart2.seviye_puani;
                   oyuncu.skor_arttir(kart2.seviye_puani);
               }
           } 

           if(oyuncuYeniDayaniklilik <= 0){ // oyuncu kartı elendi
               kullanici_kartlari.remove(kullanici_secimleri.get(i));
               if(kart1.seviye_puani < 10){
                   bilgisayar_secimleri.get(i).seviye_puani += 10;
                   bilgisayar.skor_arttir(10);
               }
               else {
                   bilgisayar_secimleri.get(i).seviye_puani += kart1.seviye_puani;
                   bilgisayar.skor_arttir(kart1.seviye_puani);
               }
           }
        }  

        System.out.println("\n" + hamleSayisi + ". tur sonu oyuncu:");
        dosyayaYaz(hamleSayisi + ". tur sonu oyuncu:");
        yazdir(kullanici_kartlari);

        System.out.println("\n" + hamleSayisi + ". tur sonu bilgisayar:");
        dosyayaYaz(hamleSayisi + ". tur sonu bilgisayar:");
        yazdir(bilgisayar_kartlari);
        
        System.out.println("Oyuncu skoru: " + oyuncu.getSkor());
        System.out.println("Bilgisayar skoru: " + bilgisayar.getSkor());
        
        dosyayaYaz("Oyuncu skoru: " + oyuncu.getSkor());
        dosyayaYaz("Bilgisayar skoru: " + bilgisayar.getSkor());
        
        if (bilgisayar_kartlari.size() <=0) {
            System.out.println("*Oyuncu Kazandi*");
            showAlert("*Oyuncu Kazandi*");
            oyunBitti = true;
            
            dosyayaYaz("*Oyuncu Kazandi*");
            return;
        }

        if (kullanici_kartlari.size() <=0) {
            System.out.println("*Bilgisayar Kazandi*");
            showAlert("*Bilgisayar Kazandi*");
            oyunBitti = true;
            
            dosyayaYaz("*Bilgisayar Kazandi*");
            return;
        }

        if ((hamleSayisi == maksimumHamle) || oyunBitti) {
            // Skorları karşılaştır
            int oyuncuSkoru = oyuncu.getSkor();
            int bilgisayarSkoru = bilgisayar.getSkor();

            if (oyuncuSkoru < bilgisayarSkoru) {
                kazananBilgisayar();
            } else if (oyuncuSkoru > bilgisayarSkoru) {
                kazananOyuncu();
            } else {
                // Skorlar eşitse, dayanıklılıkları karşılaştır
                int oyuncuDayanikliligi = dayaniklilikHesapla(kullanici_kartlari);
                int bilgisayarDayanikliligi = dayaniklilikHesapla(bilgisayar_kartlari);
                
                System.out.println("Oyuncu Dayanikliligi: " + oyuncuDayanikliligi);
                System.out.println("Bilgisayar Dayanikliligi: " + bilgisayarDayanikliligi);
                
                dosyayaYaz("Oyuncu Dayanikliligi: " + oyuncuDayanikliligi);
                dosyayaYaz("Bilgisayar Dayanikliligi: " + bilgisayarDayanikliligi);

                if (oyuncuDayanikliligi > bilgisayarDayanikliligi) {
                    kazananOyuncu();
                } else if (oyuncuDayanikliligi < bilgisayarDayanikliligi) {
                    kazananBilgisayar();
                } else {
                    berabereBitir();
                }
            }
            
            oyunBitti = true;
        }
            
    }

    private void kazananBilgisayar() {
        System.out.println("**Bilgisayar Kazandi**");
        showAlert("**Bilgisayar Kazandi**");
        
        dosyayaYaz("**Bilgisayar Kazandi**");
    }

    private void kazananOyuncu() {
        System.out.println("**Oyuncu Kazandi**");
        showAlert("**Oyuncu Kazandi**");
        
        dosyayaYaz("**Oyuncu Kazandi**");
    }

    private void berabereBitir() {
        System.out.println("***Berabere Bitti***");
        showAlert("***Berabere Bitti***");
        
        dosyayaYaz("***Berabere Bitti***");
    }
    
    // Uyarı popup'ı gösteren metot
    private void showAlert(String str) {
        // Bilgi türünde bir uyarı oluştur
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Bilgi");
        alert.setContentText(str);

        // Uyarıyı göster
        alert.showAndWait();
    }
    
    private boolean kullanilmamisKartVarMi(){
        boolean kullanilmamisKart = false;
        for(Savas_araci kart : kullanici_kartlari){
            if(!kart.kullanildiMi)
                kullanilmamisKart = true;
        }
        return kullanilmamisKart;
    }
    
    private void ilkDegerleriOlustur(){
        // Kart listesini oluşturun
        tumKartlar = tumKartlariEkle();

        // Seviye puanına göre uygun kart listesini oluşturun
        uygunKartlar = uygunKartlariGetir(tumKartlar, seviyePuani);
        
        // Kullanıcı ve bilgisayar için kartları dağıt
        kullanici_kartlari = rastgele_kart_sec(uygunKartlar, 6);
        bilgisayar_kartlari = rastgele_kart_sec(uygunKartlar, 6);
    
    }
    
    private void dosyayaYaz(String str){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("dosya.txt", true))) { // Ekleme modunda aç
            writer.write(str);
            writer.newLine(); // Yeni satır
            //System.out.println("Satırlar başarıyla eklendi.");
        } catch (IOException e) {
            System.out.println("Bir hata oluştu: " + e.getMessage());
        }
    }
}