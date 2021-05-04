package com.github.detb.trekkie.ui.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.github.detb.trekkie.db.UserRepository;

public class RegisterUserViewModel extends AndroidViewModel {

    private final UserRepository userRepository;

    public RegisterUserViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
    }
}
