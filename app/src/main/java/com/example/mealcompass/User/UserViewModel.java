package com.example.mealcompass.User;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<String> userId = new MutableLiveData<>();
    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final MutableLiveData<String> userEmail = new MutableLiveData<>();
    private final MutableLiveData<String> userType = new MutableLiveData<>();
    private final MutableLiveData<String> userPassword = new MutableLiveData<>();
    private final MutableLiveData<String> userImageUrl = new MutableLiveData<>();
    private final MutableLiveData<List<User>> userAllergens = new MutableLiveData<>();
    private final MutableLiveData<List<User>> userCuisines = new MutableLiveData<>();
    private final MutableLiveData<List<User>> userDiets = new MutableLiveData<>();
    private final MutableLiveData<List<User>> favouriteRestaurants = new MutableLiveData<>();
    private final MutableLiveData<List<User>> ownerRestaurants = new MutableLiveData<>();

    private final MutableLiveData<List<User>> userListLiveData = new MutableLiveData<>();
    private final UserRepository userRepository;

    public UserViewModel() {
        userRepository = new UserRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance());
    }

    public LiveData<String> getUserId() {
        return userId;
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<String> getUserEmail() {
        return userEmail;
    }

    public LiveData<String> getUserType() {
        return userType;
    }

    public LiveData<String> getUserPassword() {
        return userPassword;
    }

    public LiveData<String> getUserImageUrl() {
        return userImageUrl;
    }

    public LiveData<List<User>> getUserAllergens() {
        return userAllergens;
    }

    public LiveData<List<User>> getUserCuisines() {
        return userCuisines;
    }

    public LiveData<List<User>> getUserDiets() {
        return userDiets;
    }

    public LiveData<List<User>> getFavouriteRestaurants() {
        return favouriteRestaurants;
    }

    public LiveData<List<User>> getOwnerRestaurants() {
        return ownerRestaurants;
    }

    public LiveData<List<User>> getUserListLiveData() {
        return userListLiveData;
    }

    public void setUserId(String userId) {
        this.userId.setValue(userId);
    }

    public void setUserName(String userName) {
        this.userName.setValue(userName);
    }

    public void setUserEmail(String userEmail) {
        this.userEmail.setValue(userEmail);
    }

    public void setUserType(String userType) {
        this.userType.setValue(userType);
    }

    public void setUserPassword(String userPassword) {
        this.userPassword.setValue(userPassword);
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl.setValue(userImageUrl);
    }

    public void setUserAllergens(List<User> userAllergens) {
        this.userAllergens.setValue(userAllergens);
    }

    public void setUserCuisines(List<User> userCuisines) {
        this.userCuisines.setValue(userCuisines);
    }

    public void setUserDiets(List<User> userDiets) {
        this.userDiets.setValue(userDiets);
    }

    public void setFavouriteRestaurants(List<User> favouriteRestaurants) {
        this.favouriteRestaurants.setValue(favouriteRestaurants);
    }

    public void setOwnerRestaurants(List<User> ownerRestaurants) {
        this.ownerRestaurants.setValue(ownerRestaurants);
    }


    public void addFavouriteRestaurant(User restaurant) {
        List<User> currentFavourites = favouriteRestaurants.getValue();
        currentFavourites.add(restaurant);
        favouriteRestaurants.setValue(currentFavourites);
    }

    public void removeFavouriteRestaurant(User restaurant) {
        List<User> currentFavourites = favouriteRestaurants.getValue();
        currentFavourites.remove(restaurant);
        favouriteRestaurants.setValue(currentFavourites);
    }

    // get all users
    public void getAllUsers() {
        userRepository.getAllUsers(new UserRepository.UserListCallback() {
            @Override
            public void onSuccess(List<User> userList) {
                userListLiveData.setValue(userList);
            }

            @Override
            public void onFailure(Exception e) {
                // handle error
            }
        });
    }

    // get user by id
    public void getUserById(String userId) {
        userRepository.getUserById(userId, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(User user) {
                setUserId(user.getUserId());
                setUserName(user.getUserName());
                setUserEmail(user.getUserEmail());
                setUserType(user.getUserType());
                setUserImageUrl(user.getUserImageUrl());
            }

            @Override
            public void onFailure(Exception e) {
                // handle error
            }
        });
    }






}
