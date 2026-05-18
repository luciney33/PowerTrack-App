package com.example.powertrack_app

import com.example.powertrack_app.core.NetworkResultTest
import com.example.powertrack_app.data.SessionManagerTest
import com.example.powertrack_app.data.repository.AuthRepositoryTest
import com.example.powertrack_app.data.repository.NutritionRepositoryTest
import com.example.powertrack_app.data.repository.TrainingRepositoryTest
import com.example.powertrack_app.domain.usecase.LoginUseCaseTest
import com.example.powertrack_app.domain.usecase.LogoutUseCaseTest
import com.example.powertrack_app.domain.usecase.RegisterUseCaseTest
import com.example.powertrack_app.domain.usecase.gym.DeleteEntrenamientoUseCaseTest
import com.example.powertrack_app.domain.usecase.gym.GetEjerciciosUseCaseTest
import com.example.powertrack_app.domain.usecase.gym.GetEntrenamientosUseCaseTest
import com.example.powertrack_app.domain.usecase.gym.SaveEntrenamientoUseCaseTest
import com.example.powertrack_app.ui.LoginViewModelTest
import com.example.powertrack_app.ui.RegisterViewModelTest
import com.example.powertrack_app.ui.RegistroViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(

    NetworkResultTest::class,
    SessionManagerTest::class,
    TrainingRepositoryTest::class,
    AuthRepositoryTest::class,
    NutritionRepositoryTest::class,
    LoginUseCaseTest::class,
    RegisterUseCaseTest::class,
    LogoutUseCaseTest::class,
    GetEntrenamientosUseCaseTest::class,
    SaveEntrenamientoUseCaseTest::class,
    DeleteEntrenamientoUseCaseTest::class,
    GetEjerciciosUseCaseTest::class,
    LoginViewModelTest::class,
    RegisterViewModelTest::class,
    RegistroViewModelTest::class,
)
class PowerTrackTestSuite
