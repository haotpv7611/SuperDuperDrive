package com.fpt.haotpv.SuperDuperDrive.service;

import com.fpt.haotpv.SuperDuperDrive.entity.Credential;

import java.util.List;

public interface CredentialService {

    boolean isCredentialAvailable(Credential credential);

    Credential getCredentialById(Integer id);

    List<Credential> getAllCredentialsByUser(Integer userId);

    int createCredential(Credential credential);

    int updateCredential(Credential credential);

    int deleteCredential(Integer id);
}
