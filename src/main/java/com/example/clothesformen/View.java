package com.example.clothesformen;

import com.example.clothesformen.Entity.*;
import com.example.clothesformen.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.jpa.repository.Meta;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Controller
public class View {


    @Autowired
    OrdersRepoLayer ordersRepoLayer;

    @Autowired
    ProductRepoLayer productRepoLayer;


    @Autowired
    CartRepoLayer cartRepoLayer;

    @Autowired
    AdminRepoLayer adminRepoLayer;




    @RequestMapping(value = "/overview", method = RequestMethod.GET)
    public String viewOverview(Model model) {

        List<ProductList> p = new ArrayList<ProductList>(productRepoLayer.findAll());
        List<ProductList> formals = new ArrayList<ProductList>(productRepoLayer.findByGenre("coat"));
        List<ProductList> tie = new ArrayList<ProductList>(productRepoLayer.findByGenre("tie"));
        List<ProductList> shoe = new ArrayList<ProductList>(productRepoLayer.findByGenre("shoe"));
        model.addAttribute("p", p);
        model.addAttribute("formals", formals);
        model.addAttribute("tie", tie);
        model.addAttribute("shoe", shoe);
        return "overview.html";

    }


    @RequestMapping(value = "/add-cart", method = RequestMethod.POST)
    public String addtocart(
            @RequestParam("productid") Long id) {

        ProductList p = productRepoLayer.findById(id).orElse(null);

        if (p != null) {
            CartList cartList = new CartList();
            cartList.setProductID(id);
            cartList.setQuantity(1);
            cartList.setPrice(p.getProductprice());
            cartList.setCheckout(false);


            cartRepoLayer.save(cartList);
        }

        return "redirect:/overview";

    }


    @RequestMapping(value = "/cart-open", method = RequestMethod.GET)
    public String displaycart(Model model) {

        List<CartList> cartLists = new ArrayList<>(cartRepoLayer.findAll());

        List<Cartitems> cartitem = new ArrayList<>();

        for (CartList c : cartLists) {

            if(!c.isCheckout()) {
                ProductList p = productRepoLayer.findById(c.getProductID()).orElse(null);


                if (p != null) {


                    Cartitems cartitems = new Cartitems();
                    cartitems.setCartID(c.getCartID());
                    cartitems.setProductID(c.getProductID());
                    cartitems.setPrice(p.getProductprice() * c.getQuantity());
                    cartitems.setProductName(p.getProductname());
                    cartitems.setQuantity(c.getQuantity());
                    cartitem.add(cartitems);
                }
            }


        }


        model.addAttribute("list", cartitem);

        return "viewcart.html";

    }


    @PostMapping("/cart-update")
    public String updateQty(
            @RequestParam("id") int id,
            @RequestParam("qty") int qty
    ) {
        CartList p = cartRepoLayer.findByCartID(id);

        if (p != null) {
            ProductList list = productRepoLayer.findById(p.getProductID()).orElse(null);

            if (list != null) {
                p.setQuantity(qty);
                p.setPrice(qty * p.getPrice());

            }
            cartRepoLayer.save(p);
        }

        return "redirect:/cart-open";
    }


    @RequestMapping(value = "/cart/delete", method = RequestMethod.POST)
    public String deleteitem(@RequestParam("id") int id) {
        cartRepoLayer.deleteById(id);
        return "redirect:/cart-open";
    }


    @RequestMapping(value = "/cart/checkout", method = RequestMethod.POST)
    public String makecheckout(
            @RequestParam("first") String first,
            @RequestParam("last") String last,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam("card") String card,
            @RequestParam("expire") String expire,
            @RequestParam("cvv") String cvv

    ) {


//        get cart products
        List<CartList> cartdata = cartRepoLayer.findAll();

        List<Long> id = new ArrayList<>();

//       Check for checkout true from cart

        for (CartList c : cartdata) {
            if (!c.isCheckout()) {
                id.add(c.getProductID());

            }
        }


        if (card.length() == 10 && cvv.length() == 3 && expire.length() == 5) {

            AdminOrders adminOrders = new AdminOrders();
            adminOrders.setProductid(id);
            adminOrders.setName(first + " " + last);
            adminOrders.setAddress(address);
            adminOrders.setEmail(email);
            adminOrders.setCreditcard(card);
            adminOrders.setDate(LocalDate.now());
            adminOrders.setTotalpurchase(cartRepoLayer.getTotalPrice());

            try {
                ordersRepoLayer.save(adminOrders);
            } catch (Exception c) {
                throw c;
            }

            for (CartList c : cartdata) {
                c.setCheckout(true);
                cartRepoLayer.save(c);
            }
        }
        return "redirect:/cart-open";
    }


    @RequestMapping(value = "/admin/login",method = RequestMethod.GET)
    public String adminLogin(){
        return "login.html";
    }


    @PostMapping("/admin/login")
    public String verifylogin( @RequestParam("username")String username,
                               @RequestParam("pwd")String pwd){

     Admin admin = adminRepoLayer.findByUsername(username);

     if(admin != null && Objects.equals(admin.getPassword(), pwd)){
         return "redirect:/admin/dashboard";
     }
     return "redirect:/admin/login";

    }


    @RequestMapping(value = "/admin/dashboard",method = RequestMethod.GET)
    public String admindashboard(Model model){
        List<ProductList> p = new ArrayList<>(productRepoLayer.findAll());

        List<AdminOrders> adminOrders = new ArrayList<>(ordersRepoLayer.findAll());
        model.addAttribute("items",p);
        model.addAttribute("list",adminOrders);
        return "admindash.html";
    }


    @RequestMapping(value = "/admin/update/product" , method = RequestMethod.POST)
    public String upateitem(
            @RequestParam("itemid")Long id,
            @RequestParam("pPrice")Double price,
            @RequestParam("pName")String name,
            @RequestParam("category")String category,
            @RequestParam("image")String image
    ){

        ProductList productList = productRepoLayer.findById(id).orElse(null);

        if(productList!=null){
            productList.setGenre(category);
            productList.setProductimage(image);
            productList.setProductname(name);
            productList.setProductprice(price);

            productRepoLayer.save(productList);

        }
        return "redirect:/admin/dashboard";
    }

    @RequestMapping(value = "/admin/delete",method = RequestMethod.POST)
    public String deleteitem(@RequestParam("itemid")Long id){
        productRepoLayer.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/add")
    public String addpage(){
        return "add.html";
    }
    @PostMapping("/admin/add")
    public String addpost(
            @RequestParam("productprice")Double price,
            @RequestParam("productname")String name,
            @RequestParam("genre")String category,
            @RequestParam("productimage")String image
    ){

        ProductList p = new ProductList();
        p.setGenre(category);
        p.setProductimage(image);
        p.setProductname(name);
        p.setProductprice(price);

        productRepoLayer.save(p);

        return "redirect:/admin/dashboard";

    }



}
