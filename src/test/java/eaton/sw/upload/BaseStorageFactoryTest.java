package eaton.sw.upload;

import static eaton.sw.upload.ChunkedFileUploadTestFixture.CHUNK_1;
import static eaton.sw.upload.ChunkedFileUploadTestFixture.COMPLETED_FILE_1_CHUNKS;
import static eaton.sw.upload.ChunkedFileUploadTestFixture.COMPLETED_FILE_1_DATA;
import static eaton.sw.upload.ChunkedFileUploadTestFixture.COMPLETED_FILE_1_NAME;
import static eaton.sw.upload.ChunkedFileUploadTestFixture.RECEIVED_CHUNK_1;
import static eaton.sw.upload.ChunkedFileUploadTestFixture.RECEIVED_CHUNK_2;
import static eaton.sw.upload.ChunkedFileUploadTestFixture.RECEIVED_CHUNK_3;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public abstract class BaseStorageFactoryTest {
    protected StorageFactory testFactory;
    
    @Before
    public void setUp() {
        testFactory = createStorageFactory();
    }
    
    protected abstract StorageFactory createStorageFactory();
    
    @Test
    public void testStoreChunk() throws Exception {
        StorageLocation location = testFactory.storeChunk(RECEIVED_CHUNK_1);
        assertArrayEquals(CHUNK_1, location.readAllBytes());
    }

    @Test
    public void testCombineChunks() throws Exception {
        ChunkedFile file = new ChunkedFile(COMPLETED_FILE_1_NAME, COMPLETED_FILE_1_CHUNKS);

        StorageLocation location1 = testFactory.storeChunk(RECEIVED_CHUNK_1);
        StorageLocation location2 = testFactory.storeChunk(RECEIVED_CHUNK_2);
        StorageLocation location3 = testFactory.storeChunk(RECEIVED_CHUNK_3);

        file.addChunk(1, location1);
        file.addChunk(2, location2);
        file.addChunk(3, location3);

        StorageLocation combinedChunks = testFactory.combineChunks(file);

        assertArrayEquals(COMPLETED_FILE_1_DATA, combinedChunks.readAllBytes());
    }
}
