package consoleInteraction.menu;

import console.menu.Menu;
import org.junit.Before;
import org.junit.Test;

public class MenuTest {
    Menu menu;

    @Before
    public void menuSetup() {
        menu = new Menu();
    }
    @Test
    public void testLogInMenu() {
        menu.logInMenu();
    }

    @Test
    public void testShowMenu() {
        menu.printReaderMenu();
        menu.printAuthorMenu();
        menu.printAdminMenu();
    }
}
