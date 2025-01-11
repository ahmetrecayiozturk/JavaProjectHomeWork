package org.project.services;

import org.project.App;
import org.project.models.Product;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ImageService {
    // Uygulama dizininde "images" adlı bir alt klasör oluşturulması için kullanılacak.
    private static Path imageStorageDirectory = Paths.get(App.getAppDir().toString(), "images");

    // Constructor: Bu sınıfın bir örneği oluşturulduğunda görüntü dizininin var olup olmadığını kontrol eder.
    public ImageService() {
        createImagesDirectoryIfNotExist(); // Görüntü dizinini oluştur.
    }

    /**
     * Görüntülerin saklanacağı dizinin var olup olmadığını kontrol eder.
     * Eğer dizin mevcut değilse, yeni bir dizin oluşturur.
     */
    private static void createImagesDirectoryIfNotExist() {
        try {
            if (!Files.exists(imageStorageDirectory)) {
                Files.createDirectories(imageStorageDirectory); // Yeni bir dizin oluşturur.
            }
        } catch (IOException e) {
            e.printStackTrace(); // Hata durumunda hata bilgilerini ekrana yazdırır.
        }
    }

    /**
     * Verilen bir görüntü dosyasını saklar.
     * - Görüntü dosyasının uzantısını alır.
     * - Benzersiz bir dosya adı oluşturur ve dosyayı hedef dizine taşır.
     *
     * @param imageFile Saklanacak görüntü dosyası.
     * @return Saklanan görüntünün dosya yolu.
     * @throws IOException Dosya işleme sırasında hata olursa fırlatılır.
     */
    public static Path saveImage(File imageFile) throws IOException {
        String extension = getFileExtension(imageFile); // Dosya uzantısını alır.
        String imageFileName = Math.abs(UUID.randomUUID().hashCode()) + extension; // Benzersiz bir dosya adı oluşturur.
        File targetFile = new File(imageStorageDirectory.toString(), imageFileName); // Hedef dosya yolu belirlenir.
        Path imageFilePath = imageStorageDirectory.resolve(imageFileName); // Hedef dosyanın tam yolu alınır.
        try {
            Files.copy(imageFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING); // Dosya kopyalanır.
        } catch (IOException e) {
            e.printStackTrace(); // Hata durumunda hata bilgilerini ekrana yazdırır.
            return null;
        }
        return imageFilePath; // Saklanan dosyanın yolu döndürülür.
    }

    /**
     * Bir dosyanın uzantısını alır.
     * - Dosya adındaki son noktanın ardından gelen kısmı döndürür.
     *
     * @param file Uzantısı alınacak dosya.
     * @return Dosyanın uzantısı (örneğin, ".jpg").
     */
    public static String getFileExtension(File file) {
        String fileName = file.getName(); // Dosya adını alır.
        int dotIndex = fileName.lastIndexOf("."); // Dosya adındaki son noktayı bulur.
        if (dotIndex > 0) {
            return fileName.substring(dotIndex); // Uzantıyı döndürür.
        }
        return ""; // Uzantı bulunamazsa boş bir string döndürür.
    }

    /**
     * Belirtilen bir ürüne ait görüntüyü siler.
     *
     * @param product Görüntüsü silinecek ürün.
     * @return Görüntü başarıyla silinirse true, aksi takdirde false.
     */
    public static boolean deleteImage(Product product) {
        Path imagePath = imageStorageDirectory.resolve(product.getImageUrl()); // Görüntünün tam yolu belirlenir.
        try {
            return Files.deleteIfExists(imagePath); // Görüntü dosyasını siler (eğer varsa).
        } catch (IOException e) {
            e.printStackTrace(); // Hata durumunda hata bilgilerini ekrana yazdırır.
            return false;
        }
    }

    /**
     * Belirtilen bir ürünün görüntüsünü günceller.
     * - Eski görüntüyü siler.
     * - Yeni görüntüyü kaydeder.
     *
     * @param product Görüntüsü güncellenecek ürün.
     * @param image Yeni görüntü dosyası.
     * @return Güncellenen görüntünün dosya yolu.
     * @throws IOException Dosya işleme sırasında hata olursa fırlatılır.
     */
    public static String updateImage(Product product, File image) throws IOException {
        if (deleteImage(product)) { // Önce eski görüntüyü siler.
            saveImage(image); // Yeni görüntüyü kaydeder.
        }
        return product.getImageUrl(); // Ürünün güncel görüntü yolunu döndürür.
    }

    /**
     * Belirtilen bir ürünün görüntüsünün mevcut olup olmadığını kontrol eder.
     *
     * @param product Kontrol edilecek ürün.
     * @return Görüntü mevcutsa true, değilse false.
     */
    public static boolean imageExists(Product product) {
        Path imagePath = imageStorageDirectory.resolve(product.getImageUrl()); // Görüntünün tam yolu belirlenir.
        return Files.exists(imagePath); // Görüntünün mevcut olup olmadığını kontrol eder.
    }

    /**
     * Kullanıcının bir görüntü seçmesine izin verir.
     * - Bir dosya seçim penceresi açar.
     *
     * @return Kullanıcının seçtiği dosya veya seçim yapılmadıysa null.
     * @throws IOException Dosya işleme sırasında hata olursa fırlatılır.
     */
    public static File chooseImage() throws IOException {
        JFileChooser fileChooser = new JFileChooser(); // Dosya seçici oluşturur.
        int returnValue = fileChooser.showOpenDialog(null); // Kullanıcıdan dosya seçmesini ister.
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile(); // Kullanıcının seçtiği dosyayı alır.
            return selectedFile;
        }
        return null; // Eğer kullanıcı seçim yapmazsa null döndürür.
    }

    /**
     * Görüntülerin saklandığı dizini döndürür.
     *
     * @return Görüntü saklama dizininin yolu.
     */
    public static Path getImageStorageDirectory() {
        return imageStorageDirectory; // Görüntülerin saklandığı dizinin yolu döndürülür.
    }

    /**
     * Görüntülerin saklanacağı dizini ayarlar.
     *
     * @param imageStorageDirectory Yeni görüntü saklama dizini.
     */
    public static void setImageStorageDirectory(Path imageStorageDirectory) {
        ImageService.imageStorageDirectory = imageStorageDirectory; // Yeni dizin yolu atanır.
    }
}
