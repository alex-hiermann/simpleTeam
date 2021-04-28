package Utils;

import static org.junit.jupiter.api.Assertions.*;

class BasicFunctionLibraryTest {

    @org.junit.jupiter.api.Test
    void hashPassword() {
        assertEquals(BasicFunctionLibrary.hashPassword("ILoveJava"), "35454B055CC325EA1AF2126E27707052");
    }
}