package net.lahlalia.budgetapi.services;

import net.lahlalia.budgetapi.dtos.ProfileResponse;
import net.lahlalia.budgetapi.dtos.UpdatePasswordRequest;

public interface ProfileService {
    ProfileResponse getProfile();
    void updatePassword(UpdatePasswordRequest request);
}
