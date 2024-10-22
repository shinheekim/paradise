package com.example.paradise.domain.profile.application;

import com.example.paradise.domain.profile.dao.ProfileRepository;
import com.example.paradise.domain.profile.domain.Profile;
import com.example.paradise.domain.profile.dto.ProfileResponseDto;
import com.example.paradise.domain.profile.dto.image.ProfileImageRequestDto;
import com.example.paradise.domain.profile.dto.profile.ProfileUpdateRequestDto;
import com.example.paradise.domain.user.entity.User;
import com.example.paradise.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    // 프로필 조회
    public ProfileResponseDto getProfile(Long userId) {
        // 유저 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        // 프로필 찾기
        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다."));

        return new ProfileResponseDto(profile);
    }

    // 프로필 수정
    @Transactional
    public ProfileResponseDto updateProfile(Long userId, ProfileUpdateRequestDto profileUpdateRequestDto) {
        // 유저 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        // 프로필 찾기
        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다."));

        profile.setBio(profileUpdateRequestDto.getBio());
        profileRepository.save(profile);

        return new ProfileResponseDto(profile);
    }

    // 프로필 이미지 수정
    public ProfileResponseDto updateProfileImage(Long userId, ProfileImageRequestDto profileImageRequestDto) {
        // 유저 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        // 프로필 찾기
        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("프로필을 찾을 수 없습니다."));

        profile.setProfileImage(profileImageRequestDto.getProfileImage());
        profileRepository.save(profile);

        return new ProfileResponseDto(profile);
    }
}