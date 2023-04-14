package com.example.clothesformen;

import com.example.clothesformen.Entity.Admin;
import com.example.clothesformen.repository.AdminRepoLayer;
import jdk.internal.jshell.tool.ConsoleIOContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClothesformenApplicationTests {

    @Autowired
    AdminRepoLayer adminRepoLayer;
    private ConsoleIOContext.CompletionTask mockMvc;
    public void testAddToCart() {
        // Mock data
        Long id = 1L;
        ProductList product = new ProductList();
        product.setId(id);
        product.setProductprice(50.0);

        // Mock repository behavior
        when(productRepoLayer.findById(id)).thenReturn(Optional.of(product));
        when(cartRepoLayer.save(any(CartList.class))).thenReturn(new CartList());

        // Create model object
        Model model = new ExtendedModelMap();

        // Call the controller method
        String view = addToCartController.addtocart(id, model);

        // Verify that the correct data was added to the cart repository
        ArgumentCaptor<CartList> cartCaptor = ArgumentCaptor.forClass(CartList.class);
        verify(cartRepoLayer, times(1)).save(cartCaptor.capture());
        CartList cart = cartCaptor.getValue();
        assertEquals(id, cart.getProductID());
        assertEquals(1, cart.getQuantity());
        assertEquals(product.getProductprice(), cart.getPrice(), 0.001);
        assertFalse(cart.getCheckout());

        // Verify that the controller returned the correct view
        assertEquals("redirect:/overview", view);
    }


    @Test
    public void testDeleteItem() {
        // create a test product
        ProductList product = new ProductList();
        product.setProductName("Test Product");
        product.setProductprice(50.0);
        product.setProductdescription("Test product description");
        product.setProductimage("test.jpg");
        product.setGenre("test");
        productRepoLayer.save(product);

        // get the product ID
        Long productId = product.getId();

        // delete the product
        String view = controller.deleteitem(productId);

        // verify that the product is deleted
        Optional<ProductList> deletedProduct = productRepoLayer.findById(productId);
        assertFalse(deletedProduct.isPresent());

        // verify the view name
        assertEquals("redirect:/admin/dashboard", view);
    }

    @Test
    public void testAdminLogin() throws Exception {
        String username = "admin";
        String pwd = "password";

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(pwd);

        Mockito.when(adminRepoLayer.findByUsername(username)).thenReturn(admin);


        mockMvc.perform(MockMvcRequestBuilders.get("/admin/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login.html"));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/login")
                        .param("username", username)
                        .param("pwd", pwd))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/dashboard"));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/login")
                        .param("username", username)
                        .param("pwd", "wrongpassword"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/login"));
    }

}
