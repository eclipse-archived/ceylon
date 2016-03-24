package com.redhat.ceylon.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.redhat.ceylon.common.FileUtil;

/**
 * Creates {@link Store}s based on {@code [keystore]} sections in the 
 * {@link CeylonConfig} 
 */
public class Keystores {

    private static final String SECTION_KEYSTORE = "keystore";
    
    /**
     * Configuration item the keystore file, if the particular implementation
     * supports storage in a file. 
     */
    private static final String ITEM_FILE = "file";
    private static final String DEFAULT_FILE = "keystore";
    
    /**
     * Configuration item for whether the itself Keystore is protected with a 
     * password, and/or whether the entry is protected with a password. 
     */
    private static final String ITEM_PROTECTION = "protection";
    private static final String DEFAULT_PROTECTION = "both";// store|items
    
    /**
     * Note that the Java default Keystore "jks" is unable to proteced 
     * SecretKeys, so that is not a suiatable default.
     */
    private static final String ITEM_STORE_TYPE = "store-type";
    private static final String DEFAULT_STORE_TYPE = "jceks";
    
    /**
     * The provider of the Keystore
     */
    private static final String ITEM_STORE_PROVIDER = "store-provider";
    private static final String DEFAULT_STORE_PROVIDER = "SunJCE";
    
    /** Configuration item for SecretKeyFatory algorithm used used for converting 
     * passwords into SecretKeys. This setting does not change how the 
     * Keystore protects that SecretKey. 
     */
    private static final String ITEM_KEY_FACTORY_ALGO = "key-factory-algo";
    private static final String DEFAULT_KEY_FACTORY_ALGO = "PBEWithMD5AndDES";

    /** Configuration item for SecretKeyFatory provider used for converting 
     * passwords into SecretKeys. This setting does not change how the 
     * Keystore protects that SecretKey.
     */
    private static final String ITEM_KEY_FACTORY_PROVIDER = "key-factory-provider";
    private static final String DEFAULT_KEY_FACTORY_PROVIDER = "SunJCE";

    /* We need to ensure that multiple threads can't to update the keystore
     * at the same time. We don't want to corrupt it. Using a class which 
     * should be loaded by the system classloader should be safe against
     * multiple class loaders too.
     */
    private static final Object MUTEX = KeyStore.class;

    private final CeylonConfig config;
    
    private String keystoreKey(String keystoreName, String itemName) {
        return SECTION_KEYSTORE + (keystoreName != null ? "." + keystoreName : "") + "." + itemName;
    }

    private Keystores() {
        this(CeylonConfig.get());
    }
    
    private Keystores(CeylonConfig config) {
        this.config = config;
    }
    
    private static Keystores instance;
    
    public static Keystores get() {
        if (instance == null) {
            instance = new Keystores();
        }
        return instance;
    }
    
    public static void set(Keystores keystores) {
        instance = keystores;
    }
    
    public static Keystores withConfig(CeylonConfig config) {
        return new Keystores(config);
    }
    
    /**
     * Bean representing a {@code [keystore]} section.
     */
    public class Store {
        private String filename;
        private String keyStoreType;
        private String keyStoreProvider;
        private String keyFactoryAlgorithm;
        private String keyFactoryProvider;
        private String protection;
        
        public Store(String storeFile, String keyStoreType,
                String keyStoreProvider, String keyFactoryAlgorithm,
                String keyFactoryProvider, String protection) {
            super();
            this.filename = storeFile;
            this.keyStoreType = keyStoreType;
            this.keyStoreProvider = keyStoreProvider;
            this.keyFactoryAlgorithm = keyFactoryAlgorithm;
            this.keyFactoryProvider = keyFactoryProvider;
            this.protection = protection;
        }

        /**
         * Path to the keystore file, relative to {@code ~/.ceylon}. 
         * @return
         */
        public String getFilename() {
            return filename;
        }
        
        private File getStoreFile() {
            return getFilename() != null ? new File(FileUtil.getUserDir(), getFilename()) : null;   
        }
        
        /**
         * If the keystore is configured with a file item, does this file exist?
         */
        public boolean fileExists() {
            File file = getStoreFile();
            return file == null || file.exists();
        }

        /**
         * The {@link KeyStore} type
         */
        public String getKeyStoreType() {
            return keyStoreType;
        }

        /**
         * The {@link KeyStore} provider name
         */
        public String getKeyStoreProvider() {
            return keyStoreProvider;
        }

        /**
         * The {@link SecretKeyFactory} algorithm name
         */
        public String getKeyFactoryAlgorithm() {
            return keyFactoryAlgorithm;
        }

        /**
         * The {@link SecretKeyFactory} provider name
         */
        public String getKeyFactoryProvider() {
            return keyFactoryProvider;
        }
        
        public String getProtection() {
            return protection;
        }

        private KeyStore loadKeyStore(char[] storePassword)
                throws GeneralSecurityException, IOException {
            KeyStore ks = KeyStore.getInstance(getKeyStoreType(), getKeyStoreProvider());
            FileInputStream stream = fileExists() ? new FileInputStream(getStoreFile()) : null;
            try {
                ks.load(stream , storePassword);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
            return ks;
        }
        
        private void saveKeystore(KeyStore ks, char[] storePassword)
                throws FileNotFoundException, KeyStoreException, IOException,
                NoSuchAlgorithmException, CertificateException {
            File file = getStoreFile();
            if (file != null) {
                FileOutputStream out = new FileOutputStream(file);
                try {
                    ks.store(out, storePassword);
                } finally {
                    out.close();
                }
            }
        }
        
        private String canonicalizeAlias(String keystoreAlias) {
            return keystoreAlias.toLowerCase();
        }
        
        /**
         * Gets the password with the given alias from this store
         * @param keystoreAlias The alias in the keystore of this password to get
         * @param storePassword The password for accessing the keystore
         * @param entryPassword The password for accessing the entry
         * @return The password, or null if the store doesn't contain a key 
         * for the given alias.
         * @throws GeneralSecurityException
         * @throws IOException
         */
        public char[] getPassword(String keystoreAlias, char[] storePassword, char[] entryPassword) 
                    throws GeneralSecurityException, IOException {
            keystoreAlias = canonicalizeAlias(keystoreAlias);
            SecretKeyFactory keyFac = SecretKeyFactory.getInstance(getKeyFactoryAlgorithm(), getKeyFactoryProvider());
            PBEKeySpec keySpec = null;
            synchronized (MUTEX) {
                KeyStore ks = loadKeyStore(storePassword);
                KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry)ks.getEntry(keystoreAlias, new KeyStore.PasswordProtection(entryPassword));
                if (entry != null) {
                    keySpec = (PBEKeySpec)keyFac.getKeySpec(entry.getSecretKey(), PBEKeySpec.class);
                }
            }
            return keySpec != null ? keySpec.getPassword() : null;
        }
        
        public char[] getPassword(String keystoreAlias, char[] accessPassword) throws GeneralSecurityException, IOException {
            return getPassword(keystoreAlias, accessPassword,  accessPassword);
        }
        
        /**
         * Sets the password with the given alias in this store
         * @param keystoreAlias The alias in the keystore of this password to get
         * @param storePassword The password for accessing the keystore
         * @param entryPassword The password for accessing the entry
         * @param password The password
         * @throws GeneralSecurityException
         * @throws IOException
         */
        public void setPassword(String keystoreAlias, char[] storePassword, char[] entryPassword, char[] password) 
                    throws Exception {
            keystoreAlias = canonicalizeAlias(keystoreAlias);
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
            SecretKeyFactory keyFac = SecretKeyFactory.getInstance(
                    keyFactoryAlgorithm, keyFactoryProvider);
            SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
            KeyStore.SecretKeyEntry credentials = new KeyStore.SecretKeyEntry(pbeKey);
            synchronized (MUTEX) {
                KeyStore ks = loadKeyStore(storePassword);
                ks.setEntry(keystoreAlias, credentials, entryPassword != null ? new KeyStore.PasswordProtection(entryPassword) : null);
                saveKeystore(ks, storePassword);
            }
        }
        
        public void setPassword(String keystoreAlias, char[] accessPassword, char[] password) throws Exception {
            setPassword(keystoreAlias, accessPassword, accessPassword, password);
        }
        
        public void deletePassword(String keystoreAlias, char[] storePassword) throws Exception {
            keystoreAlias = canonicalizeAlias(keystoreAlias);
            synchronized (MUTEX) {
                KeyStore ks = loadKeyStore(storePassword);
                if (ks.containsAlias(keystoreAlias)) {
                    ks.deleteEntry(keystoreAlias);
                }
                saveKeystore(ks, storePassword);   
            }
        }
    }
    
    /**
     * Gets the given names {@code [keystore]} sections from the config
     * @param keystore The name of the {@code [keystore]} section, or null for 
     * the default keystore
     * @return The Store
     */
    public Store getStore(String keystore) {
        String file = config.getOption(keystoreKey(keystore, ITEM_FILE), DEFAULT_FILE);
        String keyStoreType = config.getOption(keystoreKey(keystore, ITEM_STORE_TYPE), DEFAULT_STORE_TYPE);
        String keyStoreProvider = config.getOption(keystoreKey(keystore, ITEM_STORE_PROVIDER), DEFAULT_STORE_PROVIDER);
        String keyFactoryAlgorithm = config.getOption(keystoreKey(keystore, ITEM_KEY_FACTORY_ALGO), DEFAULT_KEY_FACTORY_ALGO);
        String keyFactoryProvider = config.getOption(keystoreKey(keystore, ITEM_KEY_FACTORY_PROVIDER), DEFAULT_KEY_FACTORY_PROVIDER);
        String protection = config.getOption(keystoreKey(keystore, ITEM_PROTECTION), DEFAULT_PROTECTION);
        return new Store(file, keyStoreType, keyStoreProvider, keyFactoryAlgorithm, keyFactoryProvider, protection);
    }
    
}
