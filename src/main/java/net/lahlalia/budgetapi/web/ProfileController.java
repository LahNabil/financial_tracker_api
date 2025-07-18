package net.lahlalia.budgetapi.web;

import lombok.RequiredArgsConstructor;
import net.lahlalia.budgetapi.dtos.ProfileResponse;
import net.lahlalia.budgetapi.dtos.UpdatePasswordRequest;
import net.lahlalia.budgetapi.services.ProfileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping(value="/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileResponse> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PutMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request) {
        profileService.updatePassword(request);
        return ResponseEntity.noContent().build();
    }
}
