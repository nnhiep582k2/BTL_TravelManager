package com.nnhiep.travelmanager.views;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.nnhiep.travelmanager.R;
import com.nnhiep.travelmanager.databinding.ActivityUserBinding;

public class UserActivity extends AppCompatActivity {
    ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if (intent != null) {
            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");
            String country = intent.getStringExtra("country");
            int imageid = intent.getIntExtra("imageid", R.drawable.shigeotokuda);
            binding.nameProfile.setText(name);
            binding.phoneProfile.setText(phone);
            binding.countryProfile.setText(country);
            binding.profileImage.setImageResource(imageid);
        }
    }
}
