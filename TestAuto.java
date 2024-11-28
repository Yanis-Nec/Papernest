mport org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.github.seleniumwire.SeleniumWireDriver;
import com.github.seleniumwire.Request;
import com.github.seleniumwire.Response;

public class LoginTest {
    public static void main(String[] args) {
        // Chemin vers le driver Chrome
        WebDriverManager.chromedriver().setup();

        // Initialisation du WebDriver avec Selenium Wire
        WebDriver driver = new SeleniumWireDriver();

        try {

            /* User enters the app "newspaper" flow */
            String baseUrl = "https://app.papernest.com/onboarding?anonymous&anonymousId=test&id_text=1&destination=newspaper";

            // Ouvrir la page de démarrage
            driver.get(baseUrl);

            /* A new page appears with a pop-in asking to start the flow >>> click on "Commencer" */
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement BouttonCommencer = wait.until(ExpectedConditions.elementToBeClickable(By.id("popin-poste-classic")));
            BouttonCommencer.click();

            /* Page 1: Complete the arrival date field */
            WebElement barreDatedem = wait.until(ExpectedConditions.elementToBeClickable(By.id("poste-subscription.begin_date")));
            barreDatedem.click();
       
            //Choisir la date du 28,  click via xpath pas idéale mais on a pas d' ID sur les dates
            WebElement dateButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[3]/div[2]/div/mat-datepicker-content/div[2]/mat-calendar/div/mat-month-view/table/tbody/tr[5]/td[4]/button/span[2]")

            //clique sur le bouton suivant
            WebElement NextButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("button_next")));
            NextButton.click();

            /* Page 2: Enter old and new addresses */
            WebElement oldAddress = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("old_housing.address")));
            oldAddress.sendKeys("52 Avenue Charles de Gaulle 95700 Roissy-en-France");

            WebElement newAddress = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("housing.address")));
            newAddress.sendKeys("45 Rue de Paris 95500 Gonesse");
            WebElement NextButton2 = wait.until(ExpectedConditions.elementToBeClickable(By.id("button_next")));
            NextButton2.click();
      
            /* Page 3: Offer page / "La Poste" offers are displayed */
            WebElement laPosteOffer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("offer_poste_6")));
            if (laPosteOffer.isDisplayed()) {
                System.out.println("L'offre La Poste est affichée.");
                LaPosteOffer.click();
            } else {
                System.out.println("L'offre La Poste n'est pas affichée.");
            }

            /* Page 4: Clients infos page */
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user.email)));
            emailField.sendKeys("Yanistest@papernest.com");

            // Vérification de l'API via Selenium Wire
            boolean apiCallVerified = false;
            for (Request request : ((SeleniumWireDriver) driver).getAllRequests()) {
                if (request.getMethod().equals("POST") && request.getUrl().contains("/api/utils/email/")) {
                    Response response = request.getResponse();
                    if (response != null && response.getStatusCode() == 200 && response.getBodyAsString().contains("\"response\": false")) {
                        apiCallVerified = true;
                        break;
                    }
                }
            }

            if (apiCallVerified) {
                System.out.println("L'appel API a retourné un code 200 avec la réponse attendue.");
            } else {
                System.out.println("L'appel API n'a pas retourné la réponse attendue.");
            }

         //Complété le numéro de téléphone et les autres infos perso
            WebElement NumTel = wait.until(ExpectedConditions.elementToBeClickable(By.id("user.phone_number"")));
            NumTel.click();

            WebElement CiviliteM = wait.until(ExpectedConditions.elementToBeClickable(By.id("user.civility-mister")));
            CiviliteM.click();

            WebElement Prenom = wait.until(ExpectedConditions.elementToBeClickable(By.id("user.first_name")));
            CiviliteM.sendKeys("Yanis");

            WebElement Nom = wait.until(ExpectedConditions.elementToBeClickable(By.id("user.last_name")));
            CiviliteM.sendKeys("Nechat");
                       
            WebElement NextButton3 = wait.until(ExpectedConditions.elementToBeClickable(By.id("button_next")));
            NextButton3.click();
          
            /* Page 5: Select "Je reçois le code par mail", then click on "Suivant" */
            WebElement boutonCodeEmail = wait.until(ExpectedConditions.elementToBeClickable(By.id("text_post_office")));
            boutonCodeEmail.click();

   WebElement NextButton4 = wait.until(ExpectedConditions.elementToBeClickable(By.id("button_next")));
            NextButton4.click();

            /* Page 6: Summary of Client informations */
            WebElement summaryInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("{poste-subscription.offer_name}")]")));
            if (summaryInfo.isDisplayed()) {
                System.out.println("Le résumé des informations client est affiché.");
            } else {
                System.out.println("Le résumé des informations client n'est pas affiché.");
            }
            WebElement NextButton4 = wait.until(ExpectedConditions.elementToBeClickable(By.id("button_next_summary")));
            NextButton4.click();
             
            /* Page 7: Final page (payment) */
            WebElement cardNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card-number")));
            cardNumber.sendKeys("4241 0102 3638 5458");

            WebElement expiration = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("card-expiry")));
            expiration.sendKeys("09/2028");

            WebElement securityCode = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("root")));
            securityCode.sendKeys("123");
   
            WebElement ValiderPaiement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("poste_subscription_charge_6")));
            ValiderPaiement.sendKeys("123");
 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fermer le navigateur
            driver.quit();
        }
    }
}
