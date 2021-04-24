package Utils;

import static org.junit.jupiter.api.Assertions.*;

class BasicFunctionLibraryTest {

    @org.junit.jupiter.api.Test
    void hashPassword() {
        assertTrue(BasicFunctionLibrary.hashPassword("ILoveJava").equals("35454B055CC325EA1AF2126E27707052"));
    }
}