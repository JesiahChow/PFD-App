package sg.edu.np.mad.ocbcapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import sg.edu.np.mad.ocbcapp.databinding.ActivityHomeBinding;
import sg.edu.np.mad.ocbcapp.databinding.ActivityMainBinding;

public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;
    //declare launcher result activity to handle permission request as camera access is needed
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if(isGranted){
                showCamera();
            }
            else{

            }
    });
    private ActivityResultLauncher<ScanOptions>qrCodeLauncher = registerForActivityResult(new ScanContract(),result -> {
        //if scan fails to register any results
        if(result.getContents() == null){
            Toast.makeText(this,"Failed to scan",Toast.LENGTH_SHORT).show();
        }
        else{
            setResult(result.getContents());
        }
    });
    private void setResult(String contents){
        binding.textView.setText(contents);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initBinding();
        initViews();
    }

    private void initViews() {
      binding.scan.setOnClickListener(view->{
      checkPermissionAndShowActivity(this);});
    }

    private void checkPermissionAndShowActivity(Context context) {
        if(ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    showCamera();
        }
        else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                Toast.makeText(context,"Camera Permission required", Toast.LENGTH_SHORT).show();

        }
        else{
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }

    }

    private void initBinding() {
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void showCamera(){
        //scan settings
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan QR Code");
        options.setCameraId(0);
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(false);

        qrCodeLauncher.launch(options);
    }
}