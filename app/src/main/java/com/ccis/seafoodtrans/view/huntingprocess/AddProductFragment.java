package com.ccis.seafoodtrans.view.huntingprocess;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.AddProductFragmentBinding;
import com.ccis.seafoodtrans.model.Product;
import com.ccis.seafoodtrans.utils.PathUtil;
import com.ccis.seafoodtrans.utils.PreferenceController;
import com.ccis.seafoodtrans.viewmodel.ProductViewModel;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddProductFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 5;
    private static final int PICK_IMAGE = 1;
    ProductViewModel model;
    AddProductFragmentBinding binding;
    boolean productObserverCalled;
    Product product;
    String imagePath = "";

    public static AddProductFragment newInstance(Product product) {

        Bundle args = new Bundle();

        AddProductFragment fragment = new AddProductFragment();
        fragment.setArguments(args);
        fragment.product = product;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_product_fragment, container, false);
        model = ViewModelProviders.of(this).get(ProductViewModel.class);
        if (!checkPermission()) {
            requestPermission();
        }
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.cameraImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        binding.productImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        if (product != null) {
            binding.okBtn.setText(getString(R.string.edit));
            binding.productImg.setVisibility(View.INVISIBLE);
            binding.cameraImg.setVisibility(View.INVISIBLE);
            binding.nameEditText.setText(product.getName());
            binding.amountEditText.setText(product.getAmount());
            binding.priceEditText.setText(product.getPrice());
            binding.weightEditText.setText(product.getWeight());
        }
        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted()) {
                    if (product == null) {//add
                        if (!binding.nameEditText.getText().toString().trim().isEmpty() &&
                                !binding.weightEditText.getText().toString().trim().isEmpty() &&
                                !binding.priceEditText.getText().toString().trim().isEmpty() &&
                                !binding.amountEditText.getText().toString().trim().isEmpty()) {
                            if (!productObserverCalled) {
                                productObserverCalled = true;
                                //String name, String price, String barcode, String weight, String amount, String fishing_date, String fisherman_id
                                Log.e("Barcode", new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));
                                Log.e("Date", new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
                                Log.e("Id", PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_ID));
                                model.addProduct(binding.nameEditText.getText().toString(),
                                        binding.priceEditText.getText().toString(),
                                        new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()),
                                        binding.weightEditText.getText().toString(),
                                        binding.amountEditText.getText().toString(),
                                        new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()),
                                        PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_ID),
                                        imagePath
                                ).observe(getViewLifecycleOwner(), new Observer<String>() {
                                    @Override
                                    public void onChanged(String s) {
                                        if (s.equals("Ok")) {
                                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                            transaction.replace(R.id.flFragment, HuntingProcessFragment.newInstance());
                                            transaction.addToBackStack(null);
                                            transaction.commit();
                                        }
                                        Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                model.addProduct(binding.nameEditText.getText().toString(),
                                        binding.priceEditText.getText().toString(),
                                        new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()),
                                        binding.weightEditText.getText().toString(),
                                        binding.amountEditText.getText().toString(),
                                        new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()),
                                        PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_ID),
                                        imagePath
                                );
                            }
                        } else {
                            Toast.makeText(requireActivity(), "InComplete Fields!", Toast.LENGTH_SHORT).show();
                        }
                    } else {//update
                        if (!binding.nameEditText.getText().toString().trim().isEmpty() &&
                                !binding.weightEditText.getText().toString().trim().isEmpty() &&
                                !binding.priceEditText.getText().toString().trim().isEmpty() &&
                                !binding.amountEditText.getText().toString().trim().isEmpty()) {
                            if (!productObserverCalled) {
                                productObserverCalled = true;
                                //String name, String price, String barcode, String weight, String amount, String fishing_date, String fisherman_id
                                model.updateProduct(product.getId(), binding.nameEditText.getText().toString(),
                                        binding.priceEditText.getText().toString(),
                                        binding.weightEditText.getText().toString(), binding.amountEditText.getText().toString(),
                                        PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_ID)
                                ).observe(getViewLifecycleOwner(), new Observer<String>() {
                                    @Override
                                    public void onChanged(String s) {
                                        if (s.equals("Ok")) {
                                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                            transaction.replace(R.id.flFragment, HuntingProcessFragment.newInstance());
                                            transaction.addToBackStack(null);
                                            transaction.commit();
                                        }
                                        Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                model.updateProduct(product.getId(), binding.nameEditText.getText().toString(),
                                        binding.priceEditText.getText().toString(),
                                        binding.weightEditText.getText().toString(), binding.amountEditText.getText().toString(),
                                        PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_ID));
                            }
                        } else {
                            Toast.makeText(requireActivity(), "InComplete Fields!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void openGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    private boolean checkPermission() {
//        Toast.makeText(getActivity(), "Start checkPermission", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            checkPermission();
            return false;
        } else {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                showMessageOKCancel("You need to allow access permissions",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermission();
                            }
                        });
            }

        }
        return true;
    }

    private boolean checkPermissions(){
        if(ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            return false;
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissions()) {
                Log.v(AddProductFragment.class.getName(),"Permission is granted");
                return true;
            } else {

                Log.v(AddProductFragment.class.getName(),"Permission is revoked");
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(AddProductFragment.class.getName(),"Permission is granted");
            return true;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(requireActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void requestPermission() {

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            try {
                imagePath = PathUtil.getPath(requireActivity(), data.getData());
                Log.e("Path", imagePath+"");
                binding.productImg.setImageURI(data.getData());
                binding.cameraImg.setVisibility(View.GONE);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
