/**
 * 
 */
package uk.ac.ox.cs.johnlyle.tpmwebsocket.message.keys;

import java.util.UUID;

import javax.trustedcomputing.TrustedComputingException;
import javax.trustedcomputing.tpm.TPMContext;
import javax.trustedcomputing.tpm.keys.IdentityKey;
import javax.trustedcomputing.tpm.keys.KeyManager;
import javax.trustedcomputing.tpm.keys.SigningKey;
import javax.trustedcomputing.tpm.keys.StorageKey;
import javax.trustedcomputing.tpm.keys.StorageRootKey;
import javax.trustedcomputing.tpm.keys.TPMKey;
import javax.trustedcomputing.tpm.structures.Secret;

import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.InvalidInputException;

/**
 * @author johl
 *
 */
public class KeyFactory {

	private static KeyFactory singleton = null;
	
	public static KeyFactory getKeyFactory() {
		if (singleton == null) {
			singleton = new KeyFactory();
		}
		return singleton;
	}
	
	private TPMContext context;
	
	public TPMKey createKeyByUsage(KeyUsage usage) throws InvalidInputException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, TrustedComputingException {
		if (usage == KeyUsage.Signing) {
			return createSigningKey();
		} else if (usage == KeyUsage.Storage) {
			return createStorageKey();
		}
		throw new InvalidInputException("This kind of key usage is impossible");
	}
	
	private KeyManager getKeyManager() throws TrustedComputingException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		TPMContext context = getContext();
		KeyManager km = context.getKeyManager();
		return km;
	}
	private void closeContext() throws TrustedComputingException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		TPMContext context = TPMContext.getInstance();
		context.close();
	}
	
	/*public IdentityKey createIdentityKey() {
		KeyManager km = getKeyManager();
		StorageRootKey srk = km.loadStorageRootKey(Secret.WELL_KNOWN_SECRET);
		IdentityKey idKey = km.
		km.storeTPMKey(srk, idKey, idKey.getUUID());
		return idKey;
	}*/
	
	public SigningKey createSigningKey() throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, TrustedComputingException {
		KeyManager km = getKeyManager();
		StorageRootKey srk = km.loadStorageRootKey(Secret.WELL_KNOWN_SECRET);
		SigningKey signingKey = km.createSigningKey(srk, Secret.WELL_KNOWN_SECRET, null, false, false, false, 1024, null);
		km.storeTPMKey(srk, signingKey, signingKey.getUUID());
		return signingKey;
		
//		Signer signer = context.getSigner();
//		byte[] data = "Hello World".getBytes();
//		byte[] signedData = signer.sign(data, signingKey);
//		if ( signer.validate(signedData, data, signingKey.getPublicKey()) ) {
//			System.out.println("Validated signed data");
//		} else {
//			System.out.println("Failed to validate signed data");
//		}
//		context.close();
		
	}
	
	public StorageKey createStorageKey() throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, TrustedComputingException {
		KeyManager km = getKeyManager();
		StorageRootKey srk = km.loadStorageRootKey(Secret.WELL_KNOWN_SECRET);
		StorageKey storageKey = km.createStorageKey(srk, Secret.WELL_KNOWN_SECRET, null, false, false, false, null);
		km.storeTPMKey(srk, storageKey, storageKey.getUUID());		
		return storageKey;
	}
	
	
	
	public TPMKey findKeyByUUID(UUID uuid) throws TrustedComputingException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvalidInputException {
		KeyManager km  = getKeyManager();
		UUID[] keys = km.getStoredTPMKeys();
		StorageRootKey srk = km.loadStorageRootKey(Secret.WELL_KNOWN_SECRET);	
		for (UUID u : keys) {
			//System.out.print("Does key : " + uuid + " match with " + u.toString());
			if (u.equals(uuid)) {
				//System.out.println("... yes.");
				TPMKey key = km.loadTPMKey(srk, u, Secret.WELL_KNOWN_SECRET );
				closeContext();
				return key;
			} else {
				//System.out.println("... no.");
			}
		}
		throw new InvalidInputException("Key with this UUID: " + uuid.toString() + " not found");
	}

	public TPMContext getContext() throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, TrustedComputingException {
		if (context == null) {
			context = TPMContext.getInstance();
			context.connect(null);
		}
		return context;
	}

	
}
