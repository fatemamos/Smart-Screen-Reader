package com.example.smartscreenreader;
// Import statements for various Android, Firebase, and OpenCV libraries
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
//import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import android.graphics.drawable.BitmapDrawable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import androidx.annotation.NonNull;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import android.speech.tts.UtteranceProgressListener;
import java.util.Queue;
import java.util.LinkedList;
import java.nio.charset.StandardCharsets;
import java.io.OutputStreamWriter;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;


import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.calib3d.Calib3d;

import java.util.*;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;




// MainActivity class for the Smart Screen Reader application
public class MainActivity extends AppCompatActivity {

    // Static initializer block for OpenCV setup
    static {
        // Check if OpenCV can be initialized in debug mode
        if (!OpenCVLoader.initDebug()) {
            // If initialization fails, log an error message
            Log.e("OpenCV", "OpenCV initialization failed");
        }
        else {
            // If initialization succeeds, log a debug message
            Log.d("OpenCV", "OpenCV initialization succeeded");
        }
    }
    // Define UI components and variables
    private ImageView imageView;
    private Button btn;
    private Button btn2;
    private TextView textView;
    private EditText editText;
    private static final int ACTIVITY_REQUEST_CODE = 1000;
    public static int index = 0;
    public final String directory4 = "/storage/self/primary/DCIM/";
    public static String Direc = "/storage/self/primary/DCIM/0.jpeg";
    public static ArrayList<String> Words = new ArrayList<>();
    public static ArrayList<String> Words2 = new ArrayList<>();
    public static ArrayList<Rect> Cords = new ArrayList<>();
    //public ArrayList<Point> Cordin = new ArrayList<>();

    public TextToSpeech t1;
    public File Dir1 = new File(Direc);

    public int flag = 0;
    public float speed = 0.85f;

    public String word;
    public Rect coordinates;
    public static int WordIndex;
    public static int SpeachIndex = 0;
    private Button btnSave;
    private float hoverX = -1;
    private float hoverY = -1;
    public  Bitmap tempBitmap;

    private static final String TAG1 = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Queue<String> wordQueue = new LinkedList<>();
    private boolean isNavigating = false;
    private String api = "AIzaSyBoYh4qw3YG_QGIu5pYbacXGxNTYPCcAkk" ;
    private Spinner selectedLanguage;
    private String[] languages = {"en", "ar", "he"};
    private static final double THRESHOLD = 0.7;
    private static final int DDEPTH = CvType.CV_32F;
    private static final int METHOD = Imgproc.TM_CCORR_NORMED;
    private static final double THRESHOLD_UPPER = 255.0;
    private static final double THRESHOLD_LOWER = 23;
    private static final Size BLUR_SIZE = new Size(3, 3);
    private static final String TAG = "OpenCV";
    public Bitmap capturedImageBitmap;
    public   Bitmap templateBitmap;
    public static String Direc1 = "C:\\Users\\Abu_Adam\\Pictures\\img4.png";
    private String targetword1 ="Arrangements";
    public int x;
    public int y ;
    public Point bottomRight ;
    public Point topLeft ;
    public boolean flagnavg = false;
    public int number=0;
    public  Mat templ;

    private float adjustedPointerX;
    private float adjustedPointerY;

    private List<Mat>templates =new ArrayList<>();
    private List<Mat>templates1 =new ArrayList<>();
    public  int[] templateIds= {R.drawable.img7};// Add your template drawable IDs here;R.drawable.img7,R.drawable.img77,
    public  int[] templateIds1= {R.drawable.img555};
    public Bitmap copyBitmap;
    public int num=0;
    public int num1=0;
    public  Bitmap resultBitmap;
    public Point bottomRight1 ;
    public Point topLeft1 ;
    public int flagcolor=0;
    public Rect coordinates2 ;
    public Rect coordinates1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Set the content view to the layout defined in activity_main.xml
        setContentView(R.layout.activity_main);

        // Initialize the Spinner for selecting languages
        selectedLanguage = findViewById(R.id.languageSpinner);

        // Initialize the ImageView and Buttons from the layout
        imageView = findViewById(R.id.imageView);
        btn = findViewById(R.id.btn);
        textView = findViewById(R.id.textView2);
        btn2 = findViewById(R.id.btnSelect);
        btnSave = findViewById(R.id.btnSave);

        // Initialize the EditText from the layout
        editText = findViewById(R.id.editText);

        // Method to set the language (implementation not shown here)
        SetLang();





// Create an ArrayAdapter to populate the language spinner with options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languages);
        selectedLanguage.setAdapter(adapter);

// Set an onTouchListener on the language spinner to detect when it's touched
        selectedLanguage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Check if the touch event is an ACTION_DOWN (i.e., when the user first touches the spinner)
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Initialize the available languages string with a prefix
                    String availableLanguages = "Available languages are ";
                    String lan1 = "English";
                    String lan2 = "Arabic";
                    String lan3 = "Hebrew";

                    // Concatenate the language names to the availableLanguages string
                    availableLanguages += lan1 + ", " + lan2 + ", " + lan3 + ",";

                    // Loop through the languages array and set the TextToSpeech (TTS) language based on the selected item
                    for (String language : languages) {
                        switch (selectedLanguage.getSelectedItem().toString()) {
                            case "EnGLISH":  // If the selected language is English, set TTS to US English
                                t1.setLanguage(Locale.US);
                                break;
                            case "Arabic":  // If the selected language is Arabic, set TTS to Arabic (Egypt)
                                t1.setLanguage(new Locale("ar", "EG"));
                                break;
                            case "Hebrew":  // If the selected language is Hebrew, set TTS to Hebrew (Israel)
                                t1.setLanguage(new Locale("he", "IL"));
                                break;
                            default:  // If none of the above, default to US English
                                t1.setLanguage(Locale.US);
                        }

                        // The following line is commented out in your original code, it could be used to speak the language name
                        // TextToSpe2(language);
                    }

                    // Remove the trailing comma from the availableLanguages string
                    availableLanguages = availableLanguages.substring(0, availableLanguages.length() - 2);

                    // Use TextToSpeech to speak out the available languages
                    TextToSpe2(availableLanguages);
                }
                return false;  // Returning false allows the event to be processed further by other listeners
            }
        });
        // Set the focus to the EditText when the activity starts
        editText.requestFocus();

// Set the next focusable view when the "forward" button is pressed
        editText.setNextFocusForwardId(R.id.editText);

// Set the next focusable view when the "down" button is pressed
        editText.setNextFocusDownId(R.id.editText);

// Set the next focusable view when the "up" button is pressed
        editText.setNextFocusUpId(R.id.editText);

// Set the next focusable view when the "left" button is pressed
        editText.setNextFocusLeftId(R.id.editText);

// Set the next focusable view when the "right" button is pressed
        editText.setNextFocusRightId(R.id.editText);

// Set a key listener to handle key events in the EditText
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
                // Check if the "Enter" key is pressed
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN) {
                    btn2.performClick();  // Simulate a button click when "Enter" is pressed
                    return true;
                }
                // Check if the "Right Arrow" key is pressed
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && keyEvent.getAction() == keyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (flag == 1) {  // If the flag is set to 1
                        if (SpeachIndex <= Words.size() - 1) {  // If there are more words to read
                            TextToSpe(Words.get(SpeachIndex));  // Speak the next word
                            SpeachIndex = SpeachIndex + 1;  // Move to the next word
                            return true;
                        } else {
                            TextToSpe2("Error End");  // Indicate that the end has been reached
                        }
                    }
                }
                // Check if the "Left Arrow" key is pressed
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && keyEvent.getAction() == keyEvent.KEYCODE_NAVIGATE_PREVIOUS) {
                    if (flag == 1) {  // If the flag is set to 1
                        if (SpeachIndex >= 1) {  // If there are previous words to read
                            TextToSpe2(Words.get(SpeachIndex - 1));  // Speak the previous word
                            SpeachIndex = SpeachIndex - 1;  // Move to the previous word
                            return true;
                        } else {
                            TextToSpe2("Error first");  // Indicate that the start has been reached
                        }
                    }
                }
                return false;  // Return false to allow other listeners to process the event
            }
        });

// Set an OnClickListener on the "btn" button to capture an image when clicked
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CaptureImage();  // Capture an image using the device camera
            }
        });

// Set an OnClickListener on the "btn2" button to handle user input when clicked
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String A = new String();
                String mess;

                A = editText.getText().toString();  // Get the text from the EditText

                // Handle the input text based on its value
                if (A.equals("1")) {
                    if (flag == 0) {  // If flag is 0, start reading words
                        editText.setText("");
                        textView.setText("1- Reading");
                        TextToSpe4(Words.toString());  // Speak the words
                    } else if (flag == 4) {
                        float want = Float.parseFloat(A);
                        speed = want;  // Set the speech speed
                        flag = 0;
                        editText.setText("");
                    }
                } else if (A.equals("2")) {
                    if (flag == 0) {  // If flag is 0, fill the document
                        editText.setText("");
                        textView.setText("2- fill document");
                        isNavigating = false;
                        mess = "You chose to fill the document, please start to move the mouse to help you";
                        TextToSpe2(mess);  // Provide instructions for filling the document
                        handleDocumentFillClickEvent();  // Handle the document filling event
                    } else if (flag == 4) {
                        float want = Float.parseFloat(A);
                        speed = want;  // Set the speech speed
                        flag = 0;
                        editText.setText("");
                    }
                } else if (A.equals("3")) {
                    if (flag == 0) {  // If flag is 0, search for a word
                        textView.setText("3- Search Word");
                        editText.setText("");
                        flagnavg = false;
                        mess = "Please write the word you want to find";
                        TextToSpe2(mess);  // Ask the user to input the word to search
                        flag = 3;
                    } else if (flag == 3) {
                        word = A.trim();  // Get the word to search
                        flag = 0;
                        if (wordLocate("HAND")) {  // Search for the word "HAND"
                            coordinates2 = Cords.get(WordIndex);
                            if (wordLocate("MOUSE")) {  // Search for the word "MOUSE"
                                coordinates1 = Cords.get(WordIndex);
                            }
                            if (wordLocate(word)) {  // Search for the user's input word
                                Rect coordinates = Cords.get(WordIndex);
                            }

                            // Example code for checking if a word is within a rectangle (commented out)
                            // boolean isWordWithinRectangle = isRectIntersect(coordinates, topLeft, bottomRight);

                            // Example logging and guidance code (commented out)
                            // if (isWordWithinRectangle) {
                            //   Log.d("WordCheck", "The word is within the rectangle.");
                            // } else {
                            //   Log.d("WordCheck", "The word is not within the rectangle.");
                            //   provideMovementGuidance(coordinates, topLeft, bottomRight);
                            // }

                            // Example code for providing word location (commented out)
                            // provideWordLocation(coordinates);
                        } else {
                            flag = 0;
                            mess = "Word not found";  // Indicate that the word was not found
                            TextToSpe2(mess);
                        }
                    }
                } else if (A.equals("4")) {
                    if (flag == 0) {  // If flag is 0, start scanning barcodes
                        textView.setText("4- Scanning Barcodes");
                        editText.setText("");
                        mess = "Scanning barcodes in the captured image.";
                        TextToSpe2(mess);  // Indicate that barcode scanning is starting
                        scanBarcodesInImage(BitmapFactory.decodeFile(Direc));  // Scan barcodes in the image
                    } else if (flag == 4) {
                        float want = Float.parseFloat(A);
                        speed = want;  // Set the speech speed
                        flag = 0;
                        editText.setText("");
                    }
                } else if (A.equals("5")) {
                    if (flag == 0) {  // If flag is 0, start translation
                        textView.setText("5 - Translation");
                        translateDrawAndReadText();  // Translate, draw, and read the text
                    }
                } else if (A.equals("6")) {
                    if (flag == 4) {
                        float want = Float.parseFloat(A);
                        speed = want;  // Set the speech speed
                        flag = 0;
                        editText.setText("");
                    }
                } else if (A.equals("7")) {
                    if (flag == 4) {
                        float want = Float.parseFloat(A);
                        speed = want;  // Set the speech speed
                        flag = 0;
                        editText.setText("");
                    }
                } else if (A.equals("8")) {
                    if (flag == 4) {
                        float want = Float.parseFloat(A);
                        speed = want;  // Set the speech speed
                        flag = 0;
                        editText.setText("");
                    }
                } else if (A.equals("9")) {
                    if (flag == 4) {
                        float want = Float.parseFloat(A);
                        speed = want;  // Set the speech speed
                        flag = 0;
                        editText.setText("");
                    }
                } else if (A.equals("10")) {
                    if (flag == 4) {
                        float want = Float.parseFloat(A);
                        speed = want;  // Set the speech speed
                        flag = 0;
                        editText.setText("");
                    }
                } else {
                    if (flag == 0) {  // Default case when no specific flag is set
                        textView.setText(A);  // Set the TextView to display the input
                        editText.setText("");  // Clear the EditText
                        mess = "select 1 for reading words, select 2 for fill document, select 3 for navigate to wanted word, select 4 for scanning QR, select 5 for translation";
                        TextToSpe2(mess);  // Provide instructions for available options
                    } else if (flag == 2) {
                        // Handle flag 2 case if needed (currently empty)
                    } else if (flag == 3) {
                        word = A.trim();  // Get the word to search
                        flag = 0;

                        // Locate the main word
                        if (wordLocate(word)) {
                            coordinates = Cords.get(WordIndex);
                            Log.d("WordCheck", "coordinates set to: " + coordinates);

                            // Locate "HAND"
                            if (wordLocate("HAND")) {
                                coordinates2 = Cords.get(WordIndex);
                                Log.d("WordCheck", "coordinates2 set to: " + coordinates2);
                            } else {
                                coordinates2 = null;
                                Log.d("WordCheck", "coordinates2 not found.");
                            }

                            // Locate "MOUSE"
                            if (wordLocate("MOUSE")) {
                                coordinates1 = Cords.get(WordIndex);
                                Log.d("WordCheck", "coordinates1 set to: " + coordinates1);
                            } else {
                                coordinates1 = null;
                                Log.d("WordCheck", "coordinates1 not found.");
                            }

                            // Check for required coordinates and provide feedback
                            if (coordinates != null) {
                                mess = "Word found";
                                TextToSpe2(mess);

                                // Proceed with further action if required coordinates are available
                                if (coordinates2 != null) {
                                    // Additional logic for handling coordinates2 if needed
                                } else {
                                    Log.w("WordCheck", "Proceeding without coordinates2.");
                                }

                                if (coordinates1 != null) {
                                    // Additional logic for handling coordinates1 if needed
                                } else {
                                    Log.w("WordCheck", "Proceeding without coordinates1.");
                                }
                            } else {
                                Log.e("WordCheck", "Null Rect object found for coordinates.");
                                TextToSpe2("Error: Unable to find the required coordinates.");
                            }
                        } else {
                            mess = "Word not found";  // Indicate that the word was not found
                            TextToSpe2(mess);
                        }
                    } else if (flag == 4) {
                        float want = Float.parseFloat(A);
                        speed = want;  // Set the speech speed
                        flag = 0;
                        editText.setText("");
                    }
                }
            }
        });

// Handle mouse hover and click events on the ImageView
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Handle generic motion events (e.g., mouse movements)
                return handleGenericMotionEvent(event);
            }
        });

// Set an OnClickListener on the "btnSave" button to handle image saving
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if write permission is granted
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission if not granted
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                } else {
                    // Save the image to the gallery (commented out in your original code)
                    // saveImageToGallery(tempBitmap);
                }
            }
        });
    }


    class MatchTemplateDemoRun {
        // Class-level variables
        Boolean use_mask = false; // Flag to determine if a mask should be used in template matching
        Mat img = new Mat(); // Matrix to hold the image data
        List<Mat> templates = new ArrayList<>(); // List of templates to match against the image
        Mat mask = new Mat(); // Matrix to hold the mask data (if used)
        int match_method = Imgproc.TM_CCOEFF; // Default matching method for template matching

        // Method to run the template matching algorithm
        public Mat run(Mat img, List<Mat> templates, Mat mask) {
            this.img = img; // Store the image matrix
            this.templates = templates; // Store the list of templates
            this.mask = mask; // Store the mask matrix
            return matchingMethod(); // Perform the matching and return the result
        }

        // Method to preprocess the image (convert to grayscale, apply Gaussian blur, and edge detection)
        private Mat preprocessImage(Mat src) {
            Mat gray = new Mat(); // Matrix to hold the grayscale image
            Mat edges = new Mat(); // Matrix to hold the edges detected in the image

            // Convert the image to grayscale
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

            // Apply GaussianBlur to reduce noise and improve edge detection
            Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);

            // Use Canny edge detection to find edges in the image
            Imgproc.Canny(gray, edges, 50, 150);

            // Return the edges detected in the image
            return edges;
        }

        // Method to perform the actual template matching
        private Mat matchingMethod() {
            Mat result = new Mat(); // Matrix to hold the result of the template matching
            Mat img_display = new Mat(); // Matrix to display the image with the matched template highlighted
            Mat processedImg = preprocessImage(img); // Preprocess the image before matching
            img.copyTo(img_display); // Copy the original image to the display matrix

            Point bestMatchLoc = null; // Variable to store the location of the best match
            double bestMatchValue = (match_method == Imgproc.TM_SQDIFF || match_method == Imgproc.TM_SQDIFF_NORMED) ? Double.MAX_VALUE : Double.MIN_VALUE; // Initialize best match value based on the matching method
            Mat bestTemplate = null; // Variable to store the best matching template

            // Iterate over each template in the list
            for (Mat templ : templates) {
                Mat processedTemplate = preprocessImage(templ); // Preprocess the template before matching

                // Calculate the size of the result matrix
                int result_cols = processedImg.cols() - processedTemplate.cols() + 1;
                int result_rows = processedImg.rows() - processedTemplate.rows() + 1;
                result.create(result_rows, result_cols, CvType.CV_32FC1); // Create the result matrix

                // Check if the method accepts a mask and if the mask should be used
                boolean method_accepts_mask = (Imgproc.TM_SQDIFF == match_method || match_method == Imgproc.TM_CCORR_NORMED);
                if (use_mask && method_accepts_mask) {
                    Imgproc.matchTemplate(processedImg, processedTemplate, result, match_method, mask); // Perform template matching with a mask
                } else {
                    Imgproc.matchTemplate(processedImg, processedTemplate, result, match_method); // Perform template matching without a mask
                }

                // Normalize the result to a range between 0 and 1
                Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
                Core.MinMaxLocResult mmr = Core.minMaxLoc(result); // Get the minimum and maximum values and their locations from the result matrix
                Point matchLoc; // Variable to store the match location
                double matchValue; // Variable to store the match value

                // Depending on the matching method, choose the appropriate match location and value
                if (match_method == Imgproc.TM_SQDIFF || match_method == Imgproc.TM_SQDIFF_NORMED) {
                    matchLoc = mmr.minLoc; // For SQDIFF methods, the best match has the minimum value
                    matchValue = mmr.minVal;
                    Log.d("ImageInfo", "matchValue1=" + matchValue); // Log the match value
                    if (matchValue < bestMatchValue) { // Check if the current match is better than the previous best match
                        bestMatchValue = matchValue;
                        bestMatchLoc = matchLoc;
                        bestTemplate = templ;
                    }
                } else {
                    matchLoc = mmr.maxLoc; // For other methods, the best match has the maximum value
                    matchValue = mmr.maxVal;
                    Log.d("ImageInfo", "matchValue2=" + matchValue); // Log the match value

                    if (matchValue >= bestMatchValue) { // Check if the current match is better than the previous best match
                        bestMatchValue = matchValue;
                        bestMatchLoc = matchLoc;
                        bestTemplate = templ;
                    }
                }
            }

            // If a best match was found, draw a rectangle around it on the display image
            if (bestMatchLoc != null && bestTemplate != null) {
                topLeft = bestMatchLoc; // Set the top-left corner of the rectangle to the match location
                bottomRight = new Point(bestMatchLoc.x + bestTemplate.cols(), bestMatchLoc.y + bestTemplate.rows()); // Calculate the bottom-right corner of the rectangle
                Log.d("ImageInfo", "Top-left corner: " + topLeft); // Log the top-left corner coordinates
                Log.d("ImageInfo", "Bottom-right corner: " + bottomRight); // Log the bottom-right corner coordinates

                // Draw the rectangle and add text depending on the value of flagcolor
                if (flagcolor == 0) {
                    Imgproc.rectangle(img_display, bestMatchLoc, bottomRight,
                            new Scalar(255, 0, 0, 255), 2, 8, 0); // Draw a blue rectangle

                    // Add text at the top of the rectangle
                    String label = "MOUSE";
                    int fontFace = Imgproc.FONT_HERSHEY_SIMPLEX;
                    double fontScale = 1;
                    int thickness = 2;
                    int baseline[] = new int[1];
                    Size textSize = Imgproc.getTextSize(label, fontFace, fontScale, thickness, baseline);

                    Point textOrg = new Point(topLeft.x, topLeft.y - baseline[0]);
                    if (textOrg.y < 0) {
                        textOrg.y = 0; // Ensure the text stays within the image bounds
                    }

                    Imgproc.putText(img_display, label, textOrg, fontFace, fontScale, new Scalar(0, 255, 0), thickness); // Draw the text label
                    flagcolor = 1; // Toggle the flagcolor to 1
                } else {
                    Imgproc.rectangle(img_display, bestMatchLoc, bottomRight,
                            new Scalar(0, 0, 255), 2, 8, 0); // Draw a red rectangle

                    // Add text at the top of the rectangle
                    String label = "HAND";
                    int fontFace = Imgproc.FONT_HERSHEY_SIMPLEX;
                    double fontScale = 1;
                    int thickness = 2;
                    int baseline[] = new int[1];
                    Size textSize = Imgproc.getTextSize(label, fontFace, fontScale, thickness, baseline);

                    Point textOrg = new Point(topLeft.x, topLeft.y - baseline[0]);
                    if (textOrg.y < 0) {
                        textOrg.y = 0; // Ensure the text stays within the image bounds
                    }

                    Imgproc.putText(img_display, label, textOrg, fontFace, fontScale, new Scalar(255, 0, 0, 255), thickness); // Draw the text label
                    flagcolor = 0; // Toggle the flagcolor back to 0
                }
            }

            // Return the image with the drawn rectangles and labels
            return img_display;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Call the superclass method to handle any activity result that might be processed by the parent class
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the request code matches the expected value (1) and the result is OK
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // If the result is OK and the request code is 1, recognize text from the captured image
            reconizeTextFromImage();

            // Capture the image again without displaying a preview to the user
            captureImageWithoutPreview();
        }
    }



    private void reconizeTextFromImage() {

        // Get the image file from the specified directory
        File imageFile = Dir1;

        // Decode the image file into a Bitmap object
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        try {
            // Create a mutable copy of the bitmap to allow drawing on it
            copyBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            // Create a Canvas object to draw on the copy of the bitmap
            Canvas canvas = new Canvas(copyBitmap);

            // Initialize a Paint object to define the style and color of the drawing
            Paint paint = new Paint();
            paint.setColor(Color.RED);  // Set the paint color to red
            paint.setStyle(Paint.Style.STROKE);  // Set the paint style to stroke (outline)
            paint.setStrokeWidth(5);  // Set the stroke width to 5 pixels

            // Convert the image file to a drawable and then to a Mat object
            Drawable drawable = convertFileToDrawable(Direc);
            Mat img = drawableToMat1(drawable);

            // Loop to perform template matching multiple times (15 iterations)
            for (int i = 0; i < 15; i++) {
                // Run the template matching process
                Mat resultMat = new MatchTemplateDemoRun().run(img, templates, new Mat());

                // Store the top-left and bottom-right points of the detected region
                topLeft1 = topLeft;
                bottomRight1 = bottomRight;

                // Run another template matching process with different templates
                Mat resultMat1 = new MatchTemplateDemoRun().run(resultMat, templates1, new Mat());

                // Convert the result matrix to a bitmap for display
                resultBitmap = Bitmap.createBitmap(resultMat1.cols(), resultMat1.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(resultMat1, resultBitmap);
            }

            // Display the final result bitmap on the ImageView
            imageView.setImageBitmap(resultBitmap);

            // Convert the final result bitmap to a FirebaseVisionImage for text recognition
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(resultBitmap);

            // Set up the text recognition options with language hints
            FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                    .setLanguageHints(Arrays.asList("he", "ar", "en"))
                    .build();

            // Initialize the text recognizer with the specified options
            FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance().getCloudTextRecognizer(options);

            // Process the image to recognize text
            recognizer.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                            // Clear the previous results
                            Words.clear();
                            Words2.clear();
                            Cords.clear();

                            // Initialize bounding box coordinates to extreme values
                            int left = Integer.MAX_VALUE;
                            int top = Integer.MAX_VALUE;
                            int right = Integer.MIN_VALUE;
                            int bottom = Integer.MIN_VALUE;

                            // Loop through detected text blocks
                            for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                                for (FirebaseVisionText.Line line : block.getLines()) {
                                    StringBuilder lineText = new StringBuilder();
                                    for (FirebaseVisionText.Element element : line.getElements()) {
                                        String word = element.getText();
                                        Rect boundingBox = element.getBoundingBox();
                                        lineText.append(word).append(" ");
                                        Words.add(word);  // Add the word to the list of recognized words
                                        Cords.add(boundingBox);  // Add the bounding box to the list of coordinates

                                        // Update the bounding box coordinates
                                        if (boundingBox.left < left) left = boundingBox.left;
                                        if (boundingBox.top < top) top = boundingBox.top;
                                        if (boundingBox.right > right) right = boundingBox.right;
                                        if (boundingBox.bottom > bottom) bottom = boundingBox.bottom;

                                        // The following code to draw rectangles around each word is commented out
                                        // canvas.drawRect(boundingBox, paint);
                                    }
                                    Words2.add(lineText.toString().trim() + "\n");  // Add the line of text to Words2
                                }
                            }

                            // Draw a rectangle around the combined bounding box if any text was found
                            if (left != Integer.MAX_VALUE && top != Integer.MAX_VALUE && right != Integer.MIN_VALUE && bottom != Integer.MIN_VALUE) {
                                int padding = 10;
                                Rect combinedBoundingBox = new Rect(
                                        left - padding,
                                        top - padding,
                                        right + padding,
                                        bottom + padding
                                );
                                canvas.drawRect(combinedBoundingBox, paint);  // Draw the combined bounding box
                            }

                            // Log the recognized lines of text
                            for (int i = 0; i < Words2.size(); i++) {
                                Log.d("RecognizedWord", "Word2: " + Words2.get(i));
                            }

                            // Log the recognized words and their positions
                            for (int i = 0; i < Words.size(); i++) {
                                if (Words.get(i).equalsIgnoreCase("Enjoy")) {
                                    Rect boundingBox = Cords.get(i);
                                    x = boundingBox.left;
                                    y = boundingBox.top;
                                    Log.d("RecognizedWord1", "Word: " + Words.get(i) + " - X: " + Cords.get(i).left + " - Y: " + Cords.get(i).top);
                                }
                                Log.d("RecognizedWord", "Word: " + Words.get(i) + " - X: " + Cords.get(i).left + " - Y: " + Cords.get(i).top);
                            }

                            // Further processing if a specific word is found
                            if (word != null) {
                                number = number + 1;
                                Log.d("ImageInfo", "number: " + number);
                                if (number == 1) {
                                    number = 0;
                                    if (wordLocate(word) && (!flagnavg)) {
                                        Rect coordinates = Cords.get(WordIndex);

                                        if (coordinates == null) {
                                            Log.e("WordCheck", "coordinates is null at WordIndex: " + WordIndex);
                                        }

                                        // Locate additional words "HAND" and "MOUSE" and their coordinates
                                        Rect coordinates2 = null;
                                        if (wordLocate("HAND")) {
                                            coordinates2 = Cords.get(WordIndex);
                                            Log.d("WordCheck", "coordinates2 set to: " + coordinates2);
                                        } else {
                                            Log.d("WordCheck", "coordinates2 not found.");
                                        }

                                        Rect coordinates1 = null;
                                        if (wordLocate("MOUSE")) {
                                            coordinates1 = Cords.get(WordIndex);
                                            Log.d("WordCheck", "coordinates1 set to: " + coordinates1);
                                        } else {
                                            Log.d("WordCheck", "coordinates1 not found.");
                                        }

                                        // Check if the word is within the rectangle formed by the bounding boxes
                                        if (coordinates != null && coordinates2 != null) {
                                            boolean isWordWithinRectangle = isRectIntersect(coordinates, coordinates2);
                                            flagnavg = isWordWithinRectangle;
                                            if (isWordWithinRectangle) {
                                                Log.d("WordCheck", "The word is within the rectangle.");
                                                textView.setText("found");
                                                TextToSpe4("found");
                                            } else {
                                                Log.d("WordCheck", "The word is not within the rectangle.");
                                                textView.setText("not found");
                                                provideMovementGuidance(coordinates, coordinates1);
                                            }
                                        } else {
                                            Log.e("WordCheck", "Null Rect object found for coordinates or coordinates2.");
                                            TextToSpe4("found");
                                        }
                                    }
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle the case when text recognition fails
                            Log.e("VisionError", "Error: ", e);
                            textView.setText("Failed: " + e.getMessage());
                        }
                    });
        } catch (Exception e) {
            // Handle exceptions during the process
            textView.setText("Failed: " + e.getMessage());
        }
    }

    private void captureImageWithoutPreview() {
        File imageFile = Dir1;  // Define the file where the image will be saved

        // Create an ImageReader instance for capturing images in JPEG format
        ImageReader imageReader = ImageReader.newInstance(1982, 1080, ImageFormat.JPEG, 1); // Set the width, height, and format of the image
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                // When an image is available, retrieve it
                Image image = reader.acquireLatestImage();
                if (image != null) {
                    // Read the image data into a byte array
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);

                    try {
                        // Save the image data to the specified file
                        FileOutputStream outputStream = new FileOutputStream(imageFile);
                        outputStream.write(bytes);
                        outputStream.close();

                        // Update the UI thread to process and display the image
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Perform actions based on the current image count (e.g., recognize text after 3 captures)
                                if (num == 3) {
                                    reconizeTextFromImage();  // Call the method to recognize text from the image
                                    num = 0;
                                } else {
                                    num = num + 1;  // Increment the counter for each capture
                                }

                                // Additional code to display the image in an ImageView, if needed
                                // Bitmap bitmap = BitmapFactory.decodeFile(Direc);
                                // imageView.setImageBitmap(bitmap);
                                // Display the captured image in an ImageView (commented out here)

                                // Convert the file to a drawable and recognize text (also commented out)
                                // Drawable drawable = convertFileToDrawable(Direc);
                                // reconizeTextFromImage();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();  // Handle exceptions during file writing
                    } finally {
                        image.close();  // Ensure the image is properly closed to release resources
                    }
                }
            }
        }, null);

        // Get the camera manager to access the camera
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            // Get the ID of the back camera
            String cameraId = cameraManager.getCameraIdList()[0];

            // Open the camera with the specified ID
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Check for camera permission and request it if necessary (commented out)
                // TODO: Consider calling ActivityCompat#requestPermissions here to request the missing permissions
                return;
            }
            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    try {
                        // Create a capture request for still images
                        CaptureRequest.Builder captureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                        captureRequestBuilder.addTarget(imageReader.getSurface());  // Set the target surface for the image capture
                        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);  // Set the control mode to auto

                        // Enable autofocus for the capture
                        captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
                        captureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);

                        // Set up periodic image capture with a delay (in this case, no delay)
                        final long captureInterval = 0;  // Delay between captures in milliseconds
                        final Handler handler = new Handler();
                        final Runnable captureRunnable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // Create a capture session and configure it
                                    camera.createCaptureSession(Collections.singletonList(imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                                        @Override
                                        public void onConfigured(CameraCaptureSession session) {
                                            try {
                                                // Set the capture session to repeat the capture request
                                                session.setRepeatingRequest(captureRequestBuilder.build(), null, null);
                                            } catch (CameraAccessException e) {
                                                e.printStackTrace();  // Handle exceptions during capture setup
                                            }
                                        }

                                        @Override
                                        public void onConfigureFailed(CameraCaptureSession session) {
                                            // Handle configuration failure
                                        }
                                    }, null);
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();  // Handle exceptions during session creation
                                }

                                // Schedule the next capture after the specified interval (commented out)
//                            handler.postDelayed(this, captureInterval);
                            }
                        };

                        // Start the periodic capture after the initial delay
                        handler.postDelayed(captureRunnable, captureInterval);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();  // Handle exceptions when accessing the camera
                    }
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    // Handle the case when the camera is disconnected
                }

                @Override
                public void onError(CameraDevice camera, int error) {
                    // Handle errors during camera operations
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();  // Handle exceptions when accessing the camera manager
        }
    }


    private boolean handleGenericMotionEvent(MotionEvent event) {
        int source = event.getSource();  // Get the input source of the event (e.g., mouse, touch)
        int action = event.getAction();  // Get the type of action that occurred (e.g., press, release)
        int buttonState = event.getButtonState();  // Get the state of the buttons (e.g., primary button pressed)

        // Log the details of the motion event for debugging
        Log.d("MotionEvent", "Event detected with source: " + source + ", action: " + action + ", buttonState: " + buttonState);

        // Check if the event is from a mouse device
        if ((source & InputDevice.SOURCE_MOUSE) == InputDevice.SOURCE_MOUSE) {
            Log.d("MotionEvent", "Event is from a mouse");

            // Handle hover move action
            if (action == MotionEvent.ACTION_HOVER_MOVE) {
                TextToSpe("yess");  // Provide audio feedback when hovering
                Log.d("MotionEvent", "Hover Move detected at: " + event.getX() + ", " + event.getY());
                hoverX = event.getX();  // Update the hover X position
                hoverY = event.getY();  // Update the hover Y position
                drawHoverIndicator();  // Draw an indicator at the hover position
                return true;  // Return true to indicate the event was handled
            }
            // Handle action down (mouse click)
            else if (action == MotionEvent.ACTION_DOWN) {
                Log.d("MotionEvent", "Action Down detected at: " + event.getX() + ", " + event.getY());
                if ((buttonState & MotionEvent.BUTTON_PRIMARY) != 0) {  // Check if the primary button is pressed
                    Log.d("MotionEvent", "Primary button is pressed");
                    handleClickEvent(event.getX(), event.getY());  // Handle the click event at the specified coordinates
                    return true;  // Return true to indicate the event was handled
                } else {
                    Log.d("MotionEvent", "Primary button is not pressed");
                }
            }
            // Handle button press action
            else if (action == MotionEvent.ACTION_BUTTON_PRESS) {
                Log.d("MotionEvent", "Button Press detected at: " + event.getX() + ", " + event.getY());
                if ((buttonState & MotionEvent.BUTTON_PRIMARY) != 0) {  // Check if the primary button is pressed
                    Log.d("MotionEvent", "Primary button is pressed");
                    handleClickEvent(event.getX(), event.getY());  // Handle the click event at the specified coordinates
                    return true;  // Return true to indicate the event was handled
                } else {
                    Log.d("MotionEvent", "Primary button is not pressed");
                }
            }
            // Handle other unhandled mouse actions
            else {
                Log.d("MotionEvent", "Unhandled mouse action: " + action);
            }
        }
        return false;  // Return false to indicate the event was not handled
    }

    private void handleClickEvent(float x, float y) {
        if (isNavigating) {  // Check if the application is in navigation mode
            handleNavigationClickEvent(x, y);  // Handle navigation-related click event
        } else {
            // If not navigating, handle the event for filling the document (commented out)
            // handleDocumentFillClickEvent(x, y);
        }
    }

    private void handleDocumentFillClickEvent() {
        final int TOLERANCE = 20;  // Tolerance for detecting clicks near a word
        StringBuilder allText = new StringBuilder();

        // Collect all words from Words2 into allText
        for (String line : Words2) {
            allText.append(line).append(".").append("\n");  // Append each line of text to the string builder
        }

        // Convert the StringBuilder to a single string and replace newline characters
        String textToSpeak = allText.toString().replace("\n", " ");

        // Show input dialog and start text-to-speech
        showInputDialog(allText.toString().trim());  // Display a dialog for user input
        speakTextWithPauses(textToSpeak, 2000);  // Speak the text with pauses between sentences
        // TextToSpe3(Words.toString());  // Optionally, speak the words (commented out)
    }

    private void handleNavigationClickEvent(float x, float y) {
        final int TOLERANCE = 20;  // Tolerance for detecting clicks near a word
        boolean wordFound = false;  // Initialize the flag to track if the word was found

        // Get image view dimensions
        int imageViewWidth = imageView.getWidth();
        int imageViewHeight = imageView.getHeight();

        // Get bitmap dimensions
        Drawable drawable = imageView.getDrawable();
        if (drawable == null) {
            return;  // Exit if the drawable is null
        }
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        // Calculate the scaling and translation factors
        float scaleX = (float) imageViewWidth / bitmapWidth;
        float scaleY = (float) imageViewHeight / bitmapHeight;
        float scale = Math.min(scaleX, scaleY);  // Use the smaller scale to maintain aspect ratio

        // Calculate the left and top padding to center the image
        float paddingX = (imageViewWidth - bitmapWidth * scale) / 2;
        float paddingY = (imageViewHeight - bitmapHeight * scale) / 2;

        // Adjust the x and y coordinates based on the scale and padding
        float adjustedX = (x - paddingX) / scale;
        float adjustedY = (y - paddingY) / scale;

        // Log the original and adjusted coordinates for debugging
        Log.d("NavigationClick", "Original X: " + x + ", Y: " + y);
        Log.d("NavigationClick", "Adjusted X: " + adjustedX + ", Y: " + adjustedY);
        Log.d("NavigationClick", "Scale: " + scale + ", PaddingX: " + paddingX + ", PaddingY: " + paddingY);

        // Iterate through the words to check if the click matches any word
        for (int i = 0; i < Words.size(); i++) {
            Rect rect = Cords.get(i);
            Rect expandedRect = new Rect(
                    rect.left - TOLERANCE,
                    rect.top - TOLERANCE,
                    rect.right + TOLERANCE,
                    rect.bottom + TOLERANCE
            );

            Log.d("NavigationClick", "Checking word: " + Words.get(i));
            Log.d("NavigationClick", "Rect: " + rect.toString() + ", Expanded Rect: " + expandedRect.toString());

            // Check if the adjusted click coordinates are within the expanded rectangle
            if (expandedRect.contains((int) adjustedX, (int) adjustedY)) {
                if (Words.get(i).equalsIgnoreCase(word)) {  // Check if the clicked word matches the target word
                    Log.d("NavigationClick", "Word found: " + word);
                    TextToSpe2("You clicked on the word: " + word);  // Provide feedback that the word was clicked
                    wordFound = true;  // Update the flag to indicate the word was found
                    isNavigating = false;  // Disable navigation mode
                    break;
                } else {
                    Log.d("NavigationClick", "Word does not match: " + Words.get(i) + " != " + word);
                    Rect wordRect = Cords.get(WordIndex);  // Get the bounding box of the target word
                    int targetX = wordRect.centerX();  // Get the X coordinate of the target word's center
                    int targetY = wordRect.centerY();  // Get the Y coordinate of the target word's center
                    Log.d("NavigationClick", "Target X: " + targetX + ", Y: " + targetY);
                    StringBuilder guidance = new StringBuilder("You clicked on a different word. ");

                    // Provide guidance to move towards the target word
                    if (adjustedX < targetX) {
                        guidance.append("Move right.");
                    } else if (adjustedX > targetX) {
                        guidance.append("Move left.");
                    }
                    if (adjustedY < targetY) {
                        guidance.append(" Move down.");
                    } else if (adjustedY > targetY) {
                        guidance.append(" Move up.");
                    }
                    TextToSpe2(guidance.toString());  // Provide the movement guidance via speech
                    break;
                }
            }
        }

        // Provide feedback if the word was not found (commented out)
        if (!wordFound) {
            //  TextToSpe("Try to move in a different direction.");
        }
    }

    private void showInputDialog(final String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fill Form Field");

        final EditText input = new EditText(this);  // Create an EditText to capture user input
        input.setText(text);  // Pre-fill the EditText with the provided text
        builder.setView(input);  // Set the EditText as the view for the dialog

        // Set the positive button (OK) to handle text input
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newText = input.getText().toString();  // Get the text entered by the user
                // updateBitmapWithText(newText, Cords);  // Update the bitmap with the new text (commented out)

                // Draw hover indicator after updating the text
                drawHoverIndicator();

                // Save the updated bitmap after the user fills the document
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                saveTextToFile(newText,"filled_text.txt");  // Save the text to a file

                // Provide feedback to the user that the text was saved
                Toast.makeText(MainActivity.this, "Text saved: " + newText, Toast.LENGTH_SHORT).show();
                t1.speak("Text saved: " + newText, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
        // Set the negative button (Cancel) to dismiss the dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();  // Cancel the dialog
            }
        });

        builder.show();  // Display the dialog
    }

    private void drawHoverIndicator() {
        if (Dir1.exists()) {  // Check if the directory exists
            Bitmap bitmap = BitmapFactory.decodeFile(Direc);  // Decode the bitmap from the file path
            Bitmap tempBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);  // Create a copy of the bitmap with ARGB_8888 config
            Canvas canvas = new Canvas(tempBitmap);  // Create a canvas to draw on the bitmap
            Paint paint = new Paint();
            paint.setColor(Color.RED);  // Set the paint color to red
            paint.setStyle(Paint.Style.STROKE);  // Set the paint style to stroke (outline)
            paint.setStrokeWidth(5);  // Set the stroke width

            // Draw rectangles around each word in the Cords list
            for (int i = 0; i < Cords.size(); i++) {
                Rect rect = Cords.get(i);
                canvas.drawRect(rect, paint);  // Draw the rectangle on the canvas
            }
            imageView.setImageBitmap(tempBitmap);  // Set the modified bitmap to the ImageView
        }
    }


    private void CaptureImage() {
        File Dir1 = new File(Direc);  // Create a File object for the directory specified by 'Direc'
        File imageFile = Dir1;  // Assign the directory to 'imageFile'
        Uri imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.smartscreenreader.fileprovider", imageFile);
        try {
            boolean n = imageFile.canWrite();  // Check if the image file can be written
            boolean m = imageFile.createNewFile();  // Create a new file if it doesn't exist

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  // Create an intent to capture an image
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);  // Specify where to save the captured image
            startActivityForResult(intent, 1);  // Start the activity to capture the image, with a request code of 1

        } catch (IOException e) {
            throw new RuntimeException(e);  // Throw a runtime exception if an error occurs
        }
    }

    private void TextToSpe(String text) {
        wordQueue.clear();  // Clear the word queue
        List<String> segments = groupWordsByLanguage(text);  // Group words by language
        wordQueue.addAll(segments);  // Add the grouped words to the queue
        speakNextSegment();  // Speak the next segment in the queue
    }

    private List<String> groupWordsByLanguage(String text) {
        String[] words = text.split("\\s+");  // Split the text into words by spaces
        List<String> segments = new ArrayList<>();  // Create a list to hold the segments
        StringBuilder currentSegment = new StringBuilder();  // Create a StringBuilder to build the current segment
        String currentLanguage = detectLanguage(words[0]);  // Detect the language of the first word

        for (String word : words) {
            String wordLanguage = detectLanguage(word);  // Detect the language of the current word
            if (wordLanguage.equals(currentLanguage)) {
                currentSegment.append(word).append(" ");  // If the language matches, add the word to the current segment
            } else {
                segments.add(currentSegment.toString().trim());  // Add the current segment to the list
                currentSegment = new StringBuilder(word).append(" ");  // Start a new segment with the current word
                currentLanguage = wordLanguage;  // Update the current language
            }
        }
        // Add the last segment
        segments.add(currentSegment.toString().trim());  // Add the final segment to the list

        return segments;  // Return the list of segments
    }

    private String detectLanguage(String text) {
        if (text.matches(".[א-ת]+.")) {  // Check if the text contains Hebrew characters
            return "he";
        } else if (text.matches(".[ا-ي]+.")) {  // Check if the text contains Arabic characters
            return "ar";
        } else {
            return "en";  // Default to English
        }
    }

    private void speakNextSegment() {
        if (!wordQueue.isEmpty()) {  // Check if there are segments in the queue
            String segment = wordQueue.poll();  // Get the next segment
            String language = detectLanguage(segment);  // Detect the language of the segment
            setLanguageForText(language);  // Set the language for text-to-speech
            t1.speak(segment, TextToSpeech.QUEUE_FLUSH, null, "UniqueID");  // Speak the segment
        }
    }

    private void setLanguageForText(String language) {
        int result;
        switch (language) {
            case "he":
                result = t1.setLanguage(new Locale("he"));  // Set the language to Hebrew
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Hebrew language not supported");  // Log an error if Hebrew is not supported
                }
                break;
            case "ar":
                result = t1.setLanguage(new Locale("ar"));  // Set the language to Arabic
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Arabic language not supported");  // Log an error if Arabic is not supported
                }
                break;
            case "en":
            default:
                t1.setLanguage(Locale.US);  // Default to English (US)
                break;
        }
    }

    private void SetLang() {
        // Load templates from resources and convert them to Mat objects
        for (int id : templateIds) {
            Mat mat = drawableToMat(id);  // Convert the drawable resource to a Mat object
            templates.add(mat);  // Add the Mat object to the templates list
        }
        for (int id : templateIds1) {
            Mat mat = drawableToMat(id);  // Convert the drawable resource to a Mat object
            templates1.add(mat);  // Add the Mat object to the templates1 list
        }

        // Initialize the TextToSpeech engine
        t1 = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    t1.setSpeechRate(0.9f);  // Set the speech rate
                    int result = t1.setLanguage(Locale.US);  // Set the default language to English (US)
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "English language not supported");  // Log an error if English is not supported
                    }

                    // Check and install Hebrew language data
                    result = t1.isLanguageAvailable(new Locale("he"));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Hebrew language not supported");  // Log an error if Hebrew is not supported
                    }

                    // Check and install Arabic language data
                    result = t1.isLanguageAvailable(new Locale("ar"));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Arabic language not supported");  // Log an error if Arabic is not supported
                    }

                    // Set up a listener for the TextToSpeech engine to handle speech completion and errors
                    t1.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onStart(String utteranceId) {
                            // Do nothing when speech starts
                        }

                        @Override
                        public void onDone(String utteranceId) {
                            // Speak the next word when the current one is done
                            speakNextWord();
                        }

                        @Override
                        public void onError(String utteranceId) {
                            // Handle the error (log it, notify the user, etc.)
                        }
                    });
                } else {
                    Log.e("TTS", "Initialization failed");  // Log an error if TextToSpeech initialization fails
                }
            }
        });
    }


    private void speakNextWord() {
        if (!wordQueue.isEmpty()) {  // Check if there are words in the queue
            String word = wordQueue.poll();  // Retrieve and remove the next word from the queue
            if (word.matches(".[א-ת]+.")) {
                // Set language to Hebrew if the word contains Hebrew characters
                int result = t1.setLanguage(new Locale("he"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Hebrew language not supported");  // Log an error if Hebrew is not supported
                }
            } else if (word.matches(".[ا-ي]+.")) {
                // Set language to Arabic if the word contains Arabic characters
                int result = t1.setLanguage(new Locale("ar"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Arabic language not supported");  // Log an error if Arabic is not supported
                }
            } else {
                // Set language to English if the word contains neither Hebrew nor Arabic characters
                t1.setLanguage(Locale.US);
            }
            t1.speak(word, TextToSpeech.QUEUE_FLUSH, null, "UniqueID");  // Speak the word using TextToSpeech
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {  // Check if the request code matches the permission request code
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, proceed with saving the image (this is commented out in the code)
                //saveImageToGallery(tempBitmap);
            } else {
                Log.d(TAG, "Permission denied");  // Log a message indicating that the permission was denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();  // Show a toast message to the user
            }
        }
    }

    public static boolean wordLocate(String word) {
        for (int i = 0; i < Words.size(); i++) {
            // Check if the current word in the list contains the target word
            Log.d("RecognizedWord", "Word: " + Words.get(i));
            if (Words.get(i).contains(word)) {
                WordIndex = i;  // Set the WordIndex to the position of the word found

                return true;  // Return true if the word is found
            }
        }
        return false;  // Return false if the word is not found
    }

    private void saveTextToFile(String text, String filename) {
        // Create a directory for saving the file in the "Documents/SmartScreenReader" folder
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "SmartScreenReader");
        if (!directory.exists()) {
            directory.mkdirs();  // Create the directory if it doesn't exist
        }

        // Create the file within the directory with the specified filename
        File file = new File(directory, filename);
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            // Open a file output stream with UTF-8 encoding
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            osw.write(text);  // Write the text to the file
            osw.flush();  // Flush the output stream to ensure all data is written

            // Show a toast message indicating success
            Toast.makeText(this, "Text saved: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();  // Print the stack trace if an error occurs
            // Show a toast message indicating failure
            Toast.makeText(this, "Failed to save text", Toast.LENGTH_SHORT).show();
        } finally {
            // Close the output stream writer and file output stream
            if (osw != null) {
                try {
                    osw.close();  // Close the OutputStreamWriter
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();  // Close the FileOutputStream
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Add the text file to the gallery so it can be viewed using the gallery app
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);  // Broadcast the media scan intent to make the file available in the gallery
    }

    private void provideWordLocation(Rect coordinates) {
        int x = coordinates.centerX();  // Get the X coordinate of the word's center
        int y = coordinates.centerY();  // Get the Y coordinate of the word's center
        String guidance = getWordPositionDescription(coordinates);  // Get the description of the word's position

        TextToSpe2(guidance);  // Use text-to-speech to provide the guidance
    }

    private String getWordPositionDescription(Rect coordinates) {
        int imageWidth = imageView.getWidth();  // Get the width of the image view
        int imageHeight = imageView.getHeight();  // Get the height of the image view
        int wordCenterX = coordinates.centerX();  // Get the X coordinate of the word's center
        int wordCenterY = coordinates.centerY();  // Get the Y coordinate of the word's center

        String horizontalPosition;
        String verticalPosition;

        // Determine horizontal position of the word in the image
        if (wordCenterX < imageWidth / 3) {
            horizontalPosition = "left";
        } else if (wordCenterX < 2 * imageWidth / 3) {
            horizontalPosition = "center";
        } else {
            horizontalPosition = "right";
        }

        // Determine vertical position of the word in the image
        if (wordCenterY < imageHeight / 3) {
            verticalPosition = "top";
        } else if (wordCenterY < 2 * imageHeight / 3) {
            verticalPosition = "middle";
        } else {
            verticalPosition = "bottom";
        }

        StringBuilder description = new StringBuilder();
        description.append("The word is located around the ")
                .append(verticalPosition)
                .append(" ")
                .append(horizontalPosition)
                .append(" of the image.");

        // Add pixel distances for more precise guidance
        description.append(" Approximately at X: ")
                .append(wordCenterX)
                .append(" and Y: ")
                .append(wordCenterY)
                .append(" pixels from the top-left corner.");
        int moveX = wordCenterX - imageWidth / 2;
        int moveY = wordCenterY - imageHeight / 2;
        Log.d("MOVE", "MOVEX" + moveX + "MOVEY" + moveY);  // Log the movement required to reach the word

        return description.toString();  // Return the full description
    }

    private void scanBarcodesInImage(Bitmap bitmap) {

        Log.d("BarcodeDetection", "scanBarcodesInImage called");  // Log that the barcode scanning process has started
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);  // Convert the bitmap to a FirebaseVisionImage
        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.FORMAT_QR_CODE,  // Specify barcode formats to detect
                                FirebaseVisionBarcode.FORMAT_AZTEC,
                                FirebaseVisionBarcode.FORMAT_CODE_128,
                                FirebaseVisionBarcode.FORMAT_CODE_39,
                                FirebaseVisionBarcode.FORMAT_EAN_13,
                                FirebaseVisionBarcode.FORMAT_EAN_8)
                        .build();

        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);  // Create the barcode detector with the specified options

        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
                        Log.d("BarcodeDetection", "onSuccess called");  // Log success in barcode detection
                        if (barcodes.isEmpty()) {
                            TextToSpe2("No barcodes found.");  // Notify the user if no barcodes are found
                            Log.d("BarcodeDetection", "No barcodes found");
                        } else {
                            for (FirebaseVisionBarcode barcode : barcodes) {
                                String barcodeValue = barcode.getRawValue();  // Get the raw value of the barcode
                                TextToSpe2("Scanned barcode: " + barcodeValue);  // Speak out the scanned barcode value
                                Log.d("BarcodeDetection", "Scanned barcode: " + barcodeValue);
                                if (barcode.getValueType() == FirebaseVisionBarcode.TYPE_URL) {  // Check if the barcode is a URL
                                    FirebaseVisionBarcode.UrlBookmark urlBookmark = barcode.getUrl();
                                    String url = urlBookmark.getUrl();
                                    TextToSpe2("Opening URL: " + url);  // Notify the user that the URL is being opened
                                    openUrl(url);  // Open the URL in a browser
                                }
                                // Handle other types of barcodes if needed
                                else {
                                    // Handle non-URL barcodes (e.g., product information)
                                    Log.d("BarcodeDetection", "Not work");  // Log if the barcode is not a URL
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        TextToSpe2("Failed to scan barcodes. Error: " + e.getMessage());  // Notify the user of failure in scanning barcodes
                        Log.e("BarcodeDetection", "Failed to scan barcodes", e);  // Log the error
                    }
                });
    }

    private void openUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));  // Create an intent to open the URL in a browser
        startActivity(browserIntent);  // Start the activity to open the URL
    }

    private void translateDrawAndReadText() {
        String textToTranslate = Words2.toString();  // Get the recognized text to translate
        ExecutorService executorService = Executors.newSingleThreadExecutor();  // Create a single-threaded executor for background tasks
        Future<String> future = executorService.submit(() -> {
            try {
                Translate translate = TranslateOptions.newBuilder().setApiKey(api).build().getService();  // Initialize the translation service with an API key
                Translation translation = translate.translate(
                        textToTranslate,
                        Translate.TranslateOption.targetLanguage(selectedLanguage.getSelectedItem().toString())  // Translate to the selected language
                );
                return translation.getTranslatedText();  // Return the translated text
            } catch (Exception e) {
                e.printStackTrace();  // Print stack trace if an error occurs
                return null;
            }
        });

        executorService.submit(() -> {
            try {
                String translatedText = future.get();  // Get the translated text from the future
                if (translatedText != null) {
                    runOnUiThread(() -> {
                        // Draw the translated text on the image or display it in a TextView
                        textView.setText(translatedText);

                        // Speak the translated text
                        TextToSpe2(translatedText);
                    });
                } else {
                    runOnUiThread(() -> textView.setText("Translation failed"));  // Display an error message if translation fails
                }
            } catch (Exception e) {
                e.printStackTrace();  // Print stack trace if an error occurs
                runOnUiThread(() -> textView.setText("Translation failed"));  // Display an error message if translation fails
            } finally {
                executorService.shutdown();  // Shutdown the executor service after the task is complete
            }
        });
    }

    private void TextToSpe2(String text) {
        Log.d("LANGUGE","LANG :"+ selectedLanguage.getSelectedItem().toString());  // Log the selected language
        String translatedText = translateText(text, selectedLanguage.getSelectedItem().toString());  // Translate the text to the selected language
        t1.setSpeechRate(speed);  // Set the speech rate for the TextToSpeech engine
        t1.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null);  // Speak the translated text
    }

    private String translateText(String text, String targetLanguage) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();  // Create a single-threaded executor for background tasks
        Future<String> future = executorService.submit(() -> {
            try {
                Translate translate = TranslateOptions.newBuilder().setApiKey(api).build().getService();  // Initialize the translation service with an API key
                Translation translation = translate.translate(
                        text,
                        Translate.TranslateOption.targetLanguage(targetLanguage)  // Translate the text to the target language
                );
                return translation.getTranslatedText();  // Return the translated text
            } catch (Exception e) {
                e.printStackTrace();  // Print stack trace if an error occurs
                return text;  // Return the original text if translation fails
            }
        });
        try {
            return future.get();  // Wait for the translation task to complete and get the result
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();  // Print stack trace if an error occurs
            return text;  // Return the original text if there is an exception
        } finally {
            executorService.shutdown();  // Shutdown the executor service after the task is complete
        }
    }

    // Add this method within your MainActivity
    private Mat drawableToMat(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableId);  // Get the drawable resource by its ID
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();  // Convert the drawable to a bitmap

        // Convert Bitmap to Mat
        Mat src = new Mat();
        Utils.bitmapToMat(bitmap, src);  // Convert the bitmap to an OpenCV Mat object

        // Apply Gaussian Blur
        Mat blurred = new Mat();
        Imgproc.GaussianBlur(src, blurred, new Size(5, 5), 0);  // Apply Gaussian blur to the Mat for smoothing

        return blurred;  // Return the blurred Mat
    }

    private Mat convertFileToMat(String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);  // Decode the file at the given path into a bitmap
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);  // Convert the bitmap to an OpenCV Mat object
        return mat;  // Return the Mat
    }

    private Mat resizeMat(Mat originalMat, Size size) {
        Mat resizedMat = new Mat();
        Imgproc.resize(originalMat, resizedMat, size);  // Resize the original Mat to the specified size
        return resizedMat;  // Return the resized Mat
    }

    private Mat bitmapToMat(Bitmap bitmap) {
        Mat mat = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC3);  // Create a new Mat object with the same dimensions as the bitmap
        Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);  // Convert the bitmap to 32-bit ARGB format
        Utils.bitmapToMat(bmp32, mat);  // Convert the bitmap to an OpenCV Mat object
        return mat;  // Return the Mat
    }

    private void TryClick(float x, float y) {
        final int TOLERANCE = 20; // Tolerance value for determining if a click is within a word's bounding box
        boolean wordFound = false;
        Log.d("OPENCV", "SIZE:" + Cords.size());  // Log the size of the Cords list (number of words detected)

        // Iterate through the bounding rectangles of detected words
        for (int i = 0; i < Cords.size(); i++) {
            Rect rect = Cords.get(i);
            Rect expandedRect = new Rect(rect.left, rect.top, rect.right, rect.bottom);  // Expand the rectangle for tolerance

            if (expandedRect.contains((int) x, (int) y)) {  // Check if the click coordinates are within the expanded rectangle
                Log.d("RecognizedWord2", " - X: " + x + " - Y: " + y);

                if (Words.get(i).equalsIgnoreCase(targetword1)) {  // Check if the clicked word matches the target word
                    Log.d("NavigationClick", "Word found: " + targetword1);
                    TextToSpe2("You clicked on the word: " + word);  // Speak out the word that was clicked
                    wordFound = true;
                    isNavigating = false;
                    break;
                } else {
                    Log.d("NavigationClick", "Word does not match: " + Words.get(i) + " != " + word);
                }
            }
        }

        if (!wordFound) {
            TextToSpe("Try to move in a different direction.");  // Suggest the user try clicking in a different direction if no match was found
        }
    }

    private Drawable convertFileToDrawable(String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);  // Decode the file into a bitmap
        return new BitmapDrawable(getResources(), bitmap);  // Convert the bitmap into a drawable and return it
    }

    private Mat drawableToMat1(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();  // Convert the drawable to a bitmap
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);  // Convert the bitmap to an OpenCV Mat object

        Mat src = new Mat();
        Utils.bitmapToMat(bitmap, src);  // Convert the bitmap to an OpenCV Mat object

        // Apply Gaussian Blur
        Mat dst = new Mat();
        Imgproc.GaussianBlur(src, dst, new Size(5, 5), 0);  // Apply Gaussian blur to the Mat for smoothing

        return dst;  // Return the blurred Mat
    }

    private boolean isRectIntersect(Rect wordRect, Rect coordinates) {
        if (wordRect == null || coordinates == null) {
            Log.e("MovementGuidance", "Null Rect object passed to isRectIntersect method.");  // Log an error if any of the Rect objects is null
            return false;
        }

        int imageViewWidth = imageView.getWidth();
        int imageViewHeight = imageView.getHeight();
        Drawable drawable = imageView.getDrawable();
        if (drawable == null) {
            Log.e("MovementGuidance", "Drawable is null.");  // Log an error if the drawable is null
            return false;
        }

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        float scaleX = (float) imageViewWidth / bitmapWidth;
        float scaleY = (float) imageViewHeight / bitmapHeight;
        float scale = Math.min(scaleX, scaleY);  // Calculate the scale based on imageView and bitmap dimensions

        float paddingX = (imageViewWidth - bitmapWidth * scale) / 2;
        float paddingY = (imageViewHeight - bitmapHeight * scale) / 2;

        float wordCenterX = (wordRect.centerX() - paddingX) / scale;
        float wordCenterY = (wordRect.centerY() - paddingY) / scale;
        Log.d("MovementGuidance", "wordCenterX1:" + wordRect.top + "wordCentery1:" + wordRect.left);
        Log.d("MovementGuidance", "rectCenterX1:" + coordinates.top + "rectCentery1:" + coordinates.left);

        return Math.abs(wordRect.top - coordinates.top) < 40 && Math.abs(wordRect.left - coordinates.left) < 40;  // Check if the rectangles intersect within a tolerance
    }

    private void provideMovementGuidance(Rect wordRect, Rect coordinates) {
        if (wordRect == null || coordinates == null) {
            Log.e("MovementGuidance", "Null Rect object passed to provideMovementGuidance method.");  // Log an error if any of the Rect objects is null
            TextToSpe2("Error: Unable to provide movement guidance due to null rectangle.");
            return;
        }

        StringBuilder guidance = new StringBuilder("Move ");
        int imageViewWidth = imageView.getWidth();
        int imageViewHeight = imageView.getHeight();

        Drawable drawable = imageView.getDrawable();
        if (drawable == null) {
            Log.e("MovementGuidance", "Drawable is null.");  // Log an error if the drawable is null
            return;
        }

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        float scaleX = (float) imageViewWidth / bitmapWidth;
        float scaleY = (float) imageViewHeight / bitmapHeight;
        float scale = Math.min(scaleX, scaleY);  // Calculate the scale based on imageView and bitmap dimensions

        float paddingX = (imageViewWidth - bitmapWidth * scale) / 2;
        float paddingY = (imageViewHeight - bitmapHeight * scale) / 2;

        float wordCenterX = wordRect.left;
        float wordCenterY = wordRect.top;
        Log.d("MovementGuidance", "wordCenterX:" + wordCenterX + "wordCentery:" + wordCenterY);
        Log.d("MovementGuidance", "coordinatesX:" + coordinates.left + "coordinatesy:" + coordinates.top);

        // Provide guidance based on the word's position relative to the target coordinates
        if (wordRect.left < coordinates.left) {
            guidance.append("left, ");
        } else if (wordRect.left > coordinates.left) {
            guidance.append("right, ");
        }

        if (wordRect.top > coordinates.top) {
            guidance.append("down, ");
        } else if (wordRect.top < coordinates.top) {
            guidance.append("up, ");
        }

        if (guidance.length() > 5) {
            guidance.setLength(guidance.length() - 2);  // Remove trailing comma and space
        }

        Log.d("MovementGuidance", guidance.toString());
        TextToSpe2(guidance.toString());  // Provide the movement guidance as speech output
    }

    public static Mat removeText(Mat img) {
        if (!OpenCVLoader.initDebug()) {  // Ensure proper initialization of OpenCV
            Log.e("OpenCV", "OpenCV initialization failed");
            return img;
        }

        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);  // Convert image to grayscale

        Mat binary = new Mat();
        Imgproc.threshold(gray, binary, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);  // Use thresholding to create a binary image

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);  // Find contours of the text regions

        Mat mask = Mat.zeros(img.size(), CvType.CV_8UC1);  // Create a mask for inpainting
        for (MatOfPoint contour : contours) {
            org.opencv.core.Rect rect = Imgproc.boundingRect(contour);
            Imgproc.rectangle(mask, rect, new Scalar(255), Imgproc.FILLED);  // Draw rectangles over detected text regions
        }

        Log.d("OpenCV", "Image type: " + img.type() + ", Mask type: " + mask.type());  // Debugging: Log image and mask types

        Mat img8UC3 = new Mat();
        img.convertTo(img8UC3, CvType.CV_8UC3);  // Convert the image to 8-bit 3-channel format

        Mat mask8UC1 = new Mat();
        mask.convertTo(mask8UC1, CvType.CV_8UC1);  // Convert the mask to 8-bit 1-channel format

        Log.d("OpenCV", "Converted Image type: " + img8UC3.type() + ", Converted Mask type: " + mask8UC1.type());  // Debugging: Log image and mask types after conversion

        Mat inpainted = new Mat();
        try {
            Photo.inpaint(img8UC3, mask8UC1, inpainted, 3, Photo.INPAINT_TELEA);  // Inpaint the text regions to remove text from the image
        } catch (Exception e) {
            Log.e("OpenCV", "Inpainting failed: " + e.getMessage());
            return img;  // Return the original image if inpainting fails
        }

        return inpainted;  // Return the inpainted image
    }

    // Method to convert Bitmap to Mat
    public Mat convertBitmapToMat(Bitmap bitmap) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);  // Convert the bitmap to an OpenCV Mat object
        return mat;  // Return the Mat
    }

    private Bitmap matToBitmap(Mat mat) {
        Bitmap bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);  // Create a new bitmap with the same dimensions as the Mat
        Utils.matToBitmap(mat, bitmap);  // Convert the Mat to a bitmap
        return bitmap;  // Return the bitmap
    }

    private Mat addNoiseToAlphaChannel(Mat template) {
        List<Mat> channels = new ArrayList<>(4);
        Core.split(template, channels);  // Split the template into its channels
        Mat alphaChannel = channels.get(3);  // Get the alpha channel

        // Add noise to the alpha channel
        Mat noise = new Mat(alphaChannel.size(), alphaChannel.type());
        Core.randu(noise, 0, 255);  // Generate random noise
        Core.addWeighted(alphaChannel, 0.5, noise, 0.5, 0.0, alphaChannel);  // Add the noise to the alpha channel

        channels.set(3, alphaChannel);  // Set the modified alpha channel back into the channel list
        Mat result = new Mat();
        Core.merge(channels, result);  // Merge the channels back together to form the final image
        return result;  // Return the resulting image with added noise
    }

    private void speakTextWithPauses(String text, int pauseMillis) {
        String[] lines = text.split("\n");  // Split the text by new lines
        Timer timer = new Timer();
        int delay = 0;

        for (String line : lines) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    TextToSpe3(line);  // Ensure TextToSpeech blocks until the line is fully read
                }
            }, delay);
            delay += pauseMillis + calculateSpeechDuration(line);  // Schedule the next line with a pause
        }
    }

    private int calculateSpeechDuration(String text) {
        int wordsPerMinute = 150; // Average speaking rate
        int wordsCount = text.split("\n").length;
        return (wordsCount * 60000) / wordsPerMinute;  // Estimate the speech duration (in milliseconds) based on the length of the text
    }

    private void TextToSpe3(String text) {
        wordQueue.clear();
        wordQueue.addAll(Arrays.asList(text.split("\\r?\\n")));  // Split text into words and add to the queue
        speakNextWord();  // Speak the next word in the queue
    }

    private void TextToSpe4(String text) {
        wordQueue.clear();
        wordQueue.addAll(Arrays.asList(text.split("\\s+")));  // Split text into words and add to the queue
        speakNextWord();  // Speak the next word in the queue
    }

}