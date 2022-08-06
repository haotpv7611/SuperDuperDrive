package com.fpt.haotpv.SuperDuperDrive.service.impl;

import com.fpt.haotpv.SuperDuperDrive.entity.Credential;
import com.fpt.haotpv.SuperDuperDrive.mapper.CredentialMapper;
import com.fpt.haotpv.SuperDuperDrive.service.CredentialService;
import com.fpt.haotpv.SuperDuperDrive.service.EncryptionService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialServiceImpl implements CredentialService {

    private final CredentialMapper mapper;
    private final EncryptionService encryptionService;

    public CredentialServiceImpl(CredentialMapper mapper,
                                 EncryptionService encryptionService) {
        this.mapper = mapper;
        this.encryptionService = encryptionService;
    }

    @Override
    public Credential getCredentialById(Integer id) {

        return this.mapper.findById(id);
    }

    @Override
    public List<Credential> getAllCredentialsByUser(Integer userId) {
        List<Credential> credentialList = this.mapper.findAllByUserId(userId);
        for (Credential credential : credentialList) {
            String encryptPassword = credential.getPassword();
            credential.setEncryptPassword(encryptPassword);
            credential.setPassword(encryptionService.decryptValue(encryptPassword, credential.getKey()));
        }

        return credentialList;
    }

    @Override
    public int createCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);

        return this.mapper.insert(credential);
    }

    @Override
    public int updateCredential(Credential credential) {
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encryptedPassword);

        return this.mapper.update(credential);
    }

    @Override
    public int deleteCredential(Integer noteId) {

        return this.mapper.delete(noteId);
    }
}
