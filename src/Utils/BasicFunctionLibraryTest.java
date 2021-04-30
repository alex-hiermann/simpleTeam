package Utils;

import static org.junit.jupiter.api.Assertions.*;

class BasicFunctionLibraryTest {

    @org.junit.jupiter.api.Test
    void hashPassword() {
<<<<<<< HEAD
        assertEquals("35454B055CC325EA1AF2126E27707052", BasicFunctionLibrary.hashPassword("ILoveJava"));
=======
        assertEquals(BasicFunctionLibrary.hashPassword("ILoveJava"), "35454B055CC325EA1AF2126E27707052");
>>>>>>> 9c28d34bfab8cfa4323e39c3357b878137005f96
    }
}