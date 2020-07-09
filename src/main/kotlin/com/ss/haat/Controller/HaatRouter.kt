package com.ss.haat.Controller

import com.ss.haat.Data.*
import com.ss.haat.Interface.*
import com.ss.haat.Service.FileStorage
import com.twilio.http.TwilioRestClient
import com.twilio.rest.api.v2010.account.MessageCreator
import com.twilio.type.PhoneNumber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

@RestController
@RequestMapping("/haat")
class HaatController(val userRepo:User_Repository, val animalRepo:Animal_Repository, val imageRepo:Animal_Image_Repository, val wishRepo:Wish_Repository, val orderRepo:Order_Repository, val delivertRepo:Delivery_Repository, val cartRepo:Cart_Repository)
{
    @Autowired
    lateinit var fileStorage: FileStorage

    @PostMapping("/register")
    fun register(@RequestBody reg:register):ret
    {
        var num = userRepo.getByPhone(reg.phone)

        return try
        {
            if (num!!.name!!.isEmpty())
            {
                var kt = ret("1")
                return kt
            }
            else
            {
                var kt = ret("User already exists")
                return kt
            }
        }
        catch (e: Exception)
        {
            var tk = LocalDateTime.now().toString()+reg.phone
            tk = tk.replace(",","")
            tk = tk.replace(".","")
            tk = tk.replace(":","")
            tk = tk.replace("-","")
            var k = User(0,reg.name,reg.phone,reg.password,reg.address,reg.email,0,0,"0",tk)
            userRepo.save(k)
            var kt = ret(tk)
            return kt
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody lgn:login) : ret
    {
        var k = userRepo.getByPhone(lgn.phone)

        return try
        {
            if (k!!.phone!!.isEmpty())
            {
                var k = ret("user dosent exist")
                return k
            }
            else
            {
                if (lgn.phone==k.phone && lgn.password==k.password)
                {
                    var s = userRepo.findById(k.id_user!!)
                    if (s.isPresent)
                    {
                        var data = s.get()
                        var token = token(lgn.phone!!)
                        data.token = token
                        userRepo.save(data)
                        var k = ret(token)
                        return k
                    }
                    else
                    {
                        var k = ret("user dosent exist")
                        return k
                    }
                }
                else
                {
                    var k = ret("password didnt match")
                    return k
                }
            }
        }
        catch (e:Exception)
        {
//            if (lgn.phone==k!!.phone && lgn.password==k.password)
//            {
//                val s = userRepo.findById(k.id_user!!)
//                return if (s.isPresent)
//                {
//                    var data = s.get()
//                    var token = token(lgn.phone!!)
//                    data.token = token
//                    userRepo.save(data)
//                    var k = ret(token)
//                    k
//                }
//                else
//                {
//                    var k = ret("password didnt match")
//                    k
//                }
//            }
//            else
//            {
            var k = ret("user dosent exist")
            return k
            //}
        }
    }

    @GetMapping("/{test}")
    fun test(@PathVariable test:String):ret
    {
        var k = token_varify(test)
        var kt = ret(k.toString())
        return kt
    }

    @PostMapping("/img/{test}")
    fun uploadMultipartFile(@RequestParam("uploadfile") file: MultipartFile, model: Model,@PathVariable test:String): String {
        //Appsession.name = test

        var tk = LocalDateTime.now().toString()
        tk = tk.replace(",","")
        tk = tk.replace(".","")
        tk = tk.replace(":","")
        tk = tk.replace("-","")

        var test1 = test+"_"+tk+".png"
        var k1 = Animal_Image(0,test.toLong(),test1)
        imageRepo.save(k1)

        fileStorage.store(file, test1)
        model.addAttribute("message", "File uploaded successfully! -> filename = " + file.originalFilename)
        return test1
    }

    @PostMapping("/animals/{type}")
    fun animals(@RequestBody ter:ret,@PathVariable type:String): MutableList<Animal>
    {
        return if (token_varify(ter.name!!))
        {
            if (type=="goru"||type=="sagol")
            {
                animalRepo.getAnimalType(type)
            }
            else
            {
                animalRepo.findAll()
            }
        }
        else
        {
            Collections.emptyList()
        }
    }

    @PostMapping("/product/{id}")
    fun product(@RequestBody ter:ret,@PathVariable id:String): MutableList<Animal>
    {
        return if (token_varify(ter.name!!))
        {
            animalRepo.getByAnimal(id.toLong())
        }
        else
        {
            Collections.emptyList()
        }
    }

    @PostMapping("/image/{id}")
    fun image(@RequestBody ter:ret, @PathVariable id:String): MutableList<Animal_Image>
    {
        return if (token_varify(ter.name!!))
        {
            imageRepo.getByImage(id.toLong())
        }
        else
        {
            Collections.emptyList()
        }
    }


    @PostMapping("/animal")
    fun animal_post(
            @RequestParam("uploadfile") file: MultipartFile,
            model: Model,
            @RequestParam ("animal_name") animal_name:String,
            @RequestParam ("animal_des") animal_des:String,
            @RequestParam ("animal_price_new")animal_price_new:String,
            @RequestParam ("animal_price_old")animal_price_old:String,
            @RequestParam ("animal_type")animal_type:String,
            @RequestParam ("animal_contact")animal_contact:String,
            @RequestParam ("animal_owner")animal_owner: String,
            @RequestParam ("animal_rating") animal_rating: String,
            @RequestParam ("animal_location") animal_location:String):String
    {
        //Appsession.name = token

//        var k1 = Animal(0,animal_name,animal_des,animal_price_new,animal_price_old,animal_type,"",animal_owner.toLong(),animal_contact)
//        var k2 = animalRepo.save(k1)


        var tk = LocalDateTime.now().toString()
        tk = tk.replace(",","")
        tk = tk.replace(".","")
        tk = tk.replace(":","")
        tk = tk.replace("-","")

        var k1 = Animal(0,animal_name,animal_des,animal_price_new,animal_price_old,animal_type,"0_"+tk+".png",animal_owner.toLong(),animal_contact,animal_rating.toDouble(),animal_location)
        var k2 = animalRepo.save(k1)
        tk = k1.id_animal.toString()+"_"+tk+".png"
        var name = tk

        var k3 = Animal_Image(0,k2.id_animal,name)
        imageRepo.save(k3)


        fileStorage.store(file,name)
        model.addAttribute("message", "File uploaded successfully! -> filename = " + file.originalFilename)
        return name
    }

    @PostMapping("/search/{search}")
    fun search(@RequestBody ter:ret, @PathVariable search:String): MutableList<Animal>
    {
        if (token_varify(ter.name!!))
        {
            var animals = animalRepo.findAll()

            val search_animal : MutableList<Animal> = ArrayList()


            animals.forEach {
                if (it.animal_name!!.toLowerCase()!!.contains(search.toLowerCase())||it.animal_des!!.toLowerCase().contains(search.toLowerCase())||it.animal_type!!.toLowerCase().contains(search.toLowerCase())||search.toLowerCase().contains(it.animal_name.toLowerCase())||search.toLowerCase().contains(it.animal_des!!.toLowerCase())||search!!.toLowerCase().contains(it.animal_type!!.toLowerCase()))
                {
                    search_animal.add(it)
                }
            }

            return search_animal
        }
        else
        {
            return Collections.emptyList()
        }
    }

    @PostMapping("/wish/add/{id}")
    fun wishadd(@RequestBody ter:ret,@PathVariable id:String):ret
    {
        var retr = ret()
        if (token_varify(ter.name!!))
        {
            var user = userRepo.getByToken(ter.name)
            try
            {
                var wish = wishRepo.getWishPresent(id.toLong(),user!!.id_user!!.toLong())

                if (wish.isEmpty())
                {
                    var product = animalRepo.getByAnimal(id.toLong())
                    wishRepo.save(WishList(0,product[0].id_animal,product[0].animal_name,product[0].animal_des,product[0].animal_price_new,product[0].animal_price_old,product[0].animal_pic,user.id_user,product[0].animal_rating,product[0].animal_location))
                    retr = ret("saved")
                    return retr
                }
                else
                {
                    retr = ret("you have this in your wish list")
                    return retr
                }
            }
            catch (e:Exception)
            {
                retr = ret("you have this in your wish list")
                return retr
            }

        }
        else
        {
            retr = ret("no token")
            return retr
        }
    }

    @PostMapping("/wish/delete/{id}")
    fun wishdelete(@RequestBody ter:ret, @PathVariable id:String):ret
    {
        var retr = ret()
        if (token_varify(ter.name!!))
        {
            var user = userRepo.getByToken(ter.name)
            try
            {
                var wish = wishRepo.getWishPresent(id.toLong(),user!!.id_user!!.toLong())

//                retr = ret(wish.size.toString())
//                retr = ret(user.id_user.toString())
//                return retr
                if (wish.isNotEmpty())
                {
                    wishRepo.deleteById(wish[0].id_wish!!)
                    retr = ret("deleted")
                    return retr
                }
                else
                {
                    retr = ret("already deleted")
                    return retr
                }
            }
            catch (e:Exception)
            {
                retr = ret("deleted")
                return retr
            }

        }
        else
        {
            retr = ret("no token")
            return retr
        }
    }

    @PostMapping("/wish/list")
    fun wishlist(@RequestBody ter:ret): MutableList<WishList>
    {
        if (token_varify(ter.name!!))
        {
            var user = userRepo.getByToken(ter.name!!)
            return wishRepo.getWishUser(user!!.id_user!!)
        }
        else
        {
            return Collections.emptyList()
        }
    }

    @PostMapping("/wish/view/{id}")
    fun wishview(@RequestBody ter:ret,@PathVariable id:String):ret
    {
        if (token_varify(ter.name!!))
        {
            var user = userRepo.getByToken(ter.name)
            var wish = wishRepo.getWishPresent(id.toLong(),user!!.id_user!!)
            var trt = ret()

            try {
                if (wish.size<=0)
                {
                    trt = ret("no")
                    return trt
                }
                else
                {
                    trt = ret("yes")
                    return trt
                }
            }
            catch (e:Exception)
            {
                trt = ret("no")
                return trt
            }
        }
        else
        {
            var trt = ret("no token")
            return trt
        }
    }

    @PostMapping("/forgot")
    fun forgot(@RequestBody ter:ret):ret
    {
        try {
            var usr = userRepo.getByPhone(ter.name)
            if (usr!!.phone!!.isEmpty())
            {
                var kn = ret("User Dosent Exist")
                return  kn
            }
            else
            {
                if (usr.order_state_two<=5)
                {
                    var tm = LocalDateTime.now().toString()
                    tm = tm.replace(":","")
                    tm = tm.replace(";","")
                    tm = tm.replace(",","")
                    tm = tm.replace(".","")
                    tm = tm.replace("-","")
                    tm = tm.replace("T","")
                    tm = tm.reversed()
                    tm = tm.substring(0,7)
                    usr.password = tm
                    usr.order_state_two +=1
                    userRepo.save(usr)

                    val client = TwilioRestClient.Builder("ACfd948489f07ab6bad2d75f709d63d26f", "7ab9dc96f0834507007f1e39821b1673").build()

                    val message = MessageCreator(
                            PhoneNumber("+88"+ter.name),
                            PhoneNumber("+12059906364"),
                            "Your new password is - "+tm).create(client)

                    var kn = ret("Password Changed, You Will Be Notified Shortly")
                    return kn
                }
                else
                {
                    var kn = ret("You have reached the maximum number of times you can reset your password")
                    return kn
                }
            }
        }catch (e:Exception)
        {
            var kn = ret(e.toString())//"User Dosent Exist")
            return  kn
        }
    }

    @PostMapping("/change")
    fun change(@RequestBody chng:change):ret
    {
        if (token_varify(chng.token))
        {
            var usr = userRepo.getByToken(chng.token)
            if (usr!!.password==chng.pass_old)
            {
                var user = userRepo.findById(usr.id_user!!)
                var data = user.get()
                data.password = chng.pass_new
                userRepo.save(data)
                var kn = ret("Password Changed")
                return kn
            }
            else
            {
                var kn = ret("Password didnt match")
                return kn
            }
        }
        else
        {
            var kn = ret("no token")
            return kn
        }
    }


    @PostMapping("/user")
    fun user(@RequestBody ter:ret): User?
    {
        if (token_varify(ter.name!!))
        {
            return userRepo.getByToken(ter.name!!)
        }
        else
        {
            //var usr = User(0,"","","","","",0,0,"")
            return null
        }
    }

    @PostMapping("/user/update")
    fun userupdate(@RequestBody upd:update):ret
    {
        if (token_varify(upd.token))
        {
            var usr = userRepo.getByToken(upd.token)
            var user = userRepo.findById(usr!!.id_user!!)
            var data = user.get()

            data.name = upd.fullname
            data.email = upd.second_number.toString()
            data.address = upd.address
            userRepo.save(data)
            var kn = ret("updated")
            return kn
        }
        else
        {
            var kn = ret("no token")
            return kn
        }
    }

    @PostMapping("/order/{id}")
    fun order(@RequestBody ter:ret, @PathVariable id:String):ret
    {
        if (token_varify(ter.name!!))
        {
            var animal = animalRepo.getByAnimal(id.toLong())
            var user = userRepo.getByToken(ter.name!!)


            try {
                var order = orderRepo.getOrder(user!!.id_user!!,animal[0].id_animal!!)
                if (order.animal_name!!.isEmpty())
                {
//                var kn = ret("dosent exist")
//                return kn
                    var usr = userRepo.getByToken(ter.name)
                    var users = userRepo.findById(usr!!.id_user!!)
                    var data = users.get()
                    data.order_state_two +=1
                    userRepo.save(data)
                    var time = LocalDateTime.now().toString()
                    orderRepo.save(Order(0,user!!.id_user,animal[0].id_animal,user.name,user.address,user.phone,user.second_number.toString(),animal[0].animal_name,animal[0].animal_des,animal[0].animal_price_new,"not confirmed",animal[0].animal_rating,animal[0].animal_pic,LocalDateTime.now().dayOfMonth.toString()+"-"+LocalDateTime.now().month.toString()+"-"+LocalDateTime.now().year.toString(),animal[0].animal_location))
                    var kn = ret("Order Added")
                    return kn
                }
                else
                {
                    var kn = ret("you already have this order")
                    return kn
                }
            }catch (e:Exception)
            {
                var usr = userRepo.getByToken(ter.name)
                var users = userRepo.findById(usr!!.id_user!!)
                var data = users.get()
                data.order_state_two +=1
                userRepo.save(data)
                orderRepo.save(Order(0,user!!.id_user,animal[0].id_animal,user.name,user.address,user.phone,user.second_number.toString(),animal[0].animal_name,animal[0].animal_des,animal[0].animal_price_new,"Pending",animal[0].animal_rating,animal[0].animal_pic, LocalDateTime.now().dayOfMonth.toString()+"-"+LocalDateTime.now().month.toString()+"-"+LocalDateTime.now().year.toString(),animal[0].animal_location))
                var kn = ret("Order Added")
                return kn
            }

            //orderRepo.save(Order(0,user!!.id_user,animal[0].id_animal,user.name,user.address,user.phone,user.second_number.toString(),animal[0].animal_name,animal[0].animal_des,animal[0].animal_price_new,"",animal[0].animal_pic))
            //var kn = ret("Order Added")
            //return kn
        }
        else
        {
            var kn = ret("no token")
            return kn
        }
    }

    @PostMapping("/order/list")
    fun orderlist(@RequestBody ter:ret): MutableList<Order>
    {
        if (token_varify(ter.name!!))
        {
            var user = userRepo.getByToken(ter.name)
            return orderRepo.getOrderList(user!!.id_user!!)
        }
        else
        {
            return Collections.emptyList()
        }
    }

    @PostMapping("/order/delete/{id}")
    fun orderdelete(@RequestBody ter:ret, @PathVariable id:String):ret
    {
        if (token_varify(ter.name!!))
        {
            try {

                var user = userRepo.getByToken(ter.name)
                var order = orderRepo.getOrder(user!!.id_user!!,id.toLong())
                orderRepo.deleteById(order.id_order!!)
                var kn = ret("deleted")
                return kn

            }catch (e:Exception)
            {
                var kn = ret("not in order")
                return kn
            }
        }
        else
        {
            var kn = ret("no token")
            return kn
        }
    }

    @GetMapping("/sinron/order/confirm/{id}")
    fun orderconfirm(@PathVariable id:String):ret
    {
        var car = cartRepo.findById(id.toLong())
        var cart = car.get()
        orderRepo.save(Order(0,cart.user_id,cart.animal_id,cart.user_name,cart.user_address,cart.user_number,cart.user_contact,cart.animal_name,cart.animal_des,cart.animal_price,"Pending",cart.animal_rating,cart.animal_pic,LocalDateTime.now().dayOfMonth.toString()+"-"+LocalDateTime.now().month.toString()+"-"+LocalDateTime.now().year.toString(),cart.animal_location))
        var kn = ret("saved")
        return kn

    }


    @GetMapping("/sinron/confirm/{id}/{month}/{day}/{hour}")
    fun deliveryconfirm(@PathVariable id:String, @PathVariable month:String, @PathVariable day:String, @PathVariable hour:String):ret
    {
        // order table PK = (id)
        var order = orderRepo.findById(id.toLong())
        var data = order.get()
        data.order_status = "Confirmed"
        orderRepo.save(data)
        //var dt = LocalDateTime.of(2020,6,28,10,10,10)
        delivertRepo.save(Order_Delivery(0,data.user_id,data.animal_id,id.toLong(),data.user_name,data.user_address,data.user_number,data.user_contact,data.animal_name,data.animal_price,data.order_status,data.animal_rating,data.animal_pic,data.order_time,data.order_location,"Recived",LocalDateTime.now().dayOfMonth.toString()+"-"+LocalDateTime.now().month.toString()+"-"+LocalDateTime.now().year.toString(),"No","No","No",data.order_id_tracking,"No", LocalDateTime.of(LocalDate.now().year,month.toInt(),day.toInt(),hour.toInt(),10,10)))
        var kn = ret("Confirmed")
        return kn
    }

    class test(var name:String? = null)
    @PostMapping("/sinron/ontheway/{id}")
    fun ontheway(@RequestBody ter:test, @PathVariable id:String):ret
    {
        // delivery table PK = (id)
        var delivery = delivertRepo.findById(id.toLong())
        var del = delivery.get()
        del.on_the_way = "Yes"
        del.on_the_way_message = ter.name
        delivertRepo.save(del)

        var kn = ret(ter.name)
        return kn
    }

    @PostMapping("/sinron/ontheway/updates/{id}")
    fun ontheway_updates(@RequestBody ter:test, @PathVariable id:String):ret
    {
        var kn = ret(ter.name+"----"+id)
        var del = delivertRepo.findById(id.toLong())
        var delivery = del.get()
        delivery.on_the_way_message = ter.name
        delivertRepo.save(delivery)
        return kn
    }

    @GetMapping("/sinron/delivered/{id}")
    fun delevered(@PathVariable id:String):ret
    {
        // delivery table PK = (id)
        var delivery = delivertRepo.findById(id.toLong())
        var del = delivery.get()

        var order = orderRepo.findById(del.order_id!!)
        var ord = order.get()
        ord.order_status = "Delivered"
        orderRepo.save(ord)
        del.delivered = "Yes"
        delivertRepo.save(del)
        var kn = ret("Delivered")
        return kn
    }

    @GetMapping("/sinron/product")
    fun product(): MutableList<Animal>
    {
        return animalRepo.findAll()
    }

    @GetMapping("/sinron/user")
    fun user(): MutableList<User>
    {
        return userRepo.findAll()
    }

    @GetMapping("/sinron/user/delete/{id}")
    fun userdelete(@PathVariable id:String):ret
    {
        userRepo.deleteById(id.toLong())
        try {
            var ord = orderRepo.getOrderList(id.toLong())
            var crt = cartRepo.getCartlist(id.toLong())
            var dlv = delivertRepo.getDeliverylist(id.toLong())
            var wsh = wishRepo.getWishUser(id.toLong())
            ord.forEach {
                orderRepo.deleteById(it.id_order!!)
            }
            crt.forEach {
                cartRepo.deleteById(it.id_cart!!)
            }
            dlv.forEach {
                delivertRepo.deleteById(it.id_delivery!!)
            }
            wsh.forEach {
                wishRepo.deleteById(it.id_wish!!)
            }
        }catch (e:Exception)
        {
            var kn = ret("Deleted")
            return kn
        }
        var kn = ret("Deleted")
        return kn
    }

    @GetMapping("/sinron/order")
    fun order(): MutableList<Order>
    {
        return orderRepo.getPendingOrder("Pending")
    }

    @GetMapping("/sinron/ontheway")
    fun ontheway(): MutableList<Order_Delivery>
    {
        return delivertRepo.getByOrderWay("No")
    }

    @GetMapping("/sinron/ontheway_update")
    fun ontheway_update(): MutableList<Order_Delivery>
    {
        return delivertRepo.getByOrderMsg("Yes","No").asReversed()
    }

    @GetMapping("/sinron/ontheway/deliver/{id}")
    fun ontheway_deliver(@PathVariable id:String):ret
    {
        var del = delivertRepo.findById(id.toLong())
        var delivery = del.get()
        var ord = orderRepo.findById(delivery.order_id!!)
        var order = ord.get()
        order.order_status = "Delivered"
        delivery.delivered = "Yes"
        orderRepo.save(order)
        delivertRepo.save(delivery)
        var kn = ret("Delivered")
        return kn
    }

    @GetMapping("/sinron/deliverd/list")
    fun delivered_list(): MutableList<Order_Delivery>
    {
        return delivertRepo.getByOrderMsg("Yes","Yes")
    }

    @GetMapping("/sinron/product/view/{id}")
    fun productview(@PathVariable id:String): Animal
    {
        return animalRepo.getByOne(id.toLong())
    }

    @PostMapping("/track/{id}")
    fun track(@RequestBody ter:ret, @PathVariable id:String): MutableList<Order_Delivery>
    {
        if (token_varify(ter.name!!))
        {
            return delivertRepo.getByOrderId(id.toLong())
        }
        else
        {
            return Collections.emptyList()
        }
    }


    @PostMapping("/cart/view/{id}")
    fun cartview(@RequestBody ter:ret,@PathVariable id:String):ret
    {
        if (token_varify(ter.name!!))
        {
            var user = userRepo.getByToken(ter.name)
            var cart = cartRepo.getCarts(id.toLong(),user!!.id_user!!)
            var trt = ret()

            try {
                if (cart.size<=0)
                {
                    trt = ret("no")
                    return trt
                }
                else
                {
                    trt = ret("yes")
                    return trt
                }
            }
            catch (e:Exception)
            {
                trt = ret("no")
                return trt
            }

        }
        else
        {
            var trt = ret("no token")
            return trt
        }
    }

    @PostMapping("/cart")
    fun cart(@RequestBody ter:ret): MutableList<Cart>
    {
        if (token_varify(ter.name!!))
        {
            var user = userRepo.getByToken(ter.name!!)
            return cartRepo.getCartlist(user!!.id_user!!)
        }
        else
        {
            return Collections.emptyList()
        }
    }

    @PostMapping("/cart/delete/{id}")
    fun cartdelete(@RequestBody ter:ret, @PathVariable id:String): ret
    {
        if (token_varify(ter.name!!))
        {
            var user = userRepo.getByToken(ter.name!!)
            var cart = cartRepo.getCart(id.toLong(), user!!.id_user!!)
            cartRepo.deleteById(cart.id_cart!!)

            var kn = ret("Deleted")
            return kn
        }
        else
        {
            var kn = ret("no token")
            return kn
        }
    }

    @PostMapping("/cart/add/{id}")
    fun cartadd(@RequestBody ter:ret, @PathVariable id:String):ret
    {
        if (token_varify(ter.name!!))
        {
            var user = userRepo.getByToken(ter.name!!)
            var an = animalRepo.findById(id.toLong())
            if (an.isPresent)
            {
                var animal = an.get()
                cartRepo.save(Cart(0,user!!.id_user,animal.id_animal,user.name,user.address,user.phone,user.second_number,animal.animal_name,animal.animal_des,animal.animal_price_new,animal.animal_location,animal.animal_rating,animal.animal_pic))
                var kn = ret("Added to Cart")
                return kn
            }
            else
            {
                var kn = ret("Product is not available")
                return kn
            }
        }
        else
        {
            var kn = ret("no token")
            return kn
        }
    }

    @PostMapping("/orderdetail/{id}")
    fun orderdetail(@RequestBody ter:ret, @PathVariable id:String):orderdetail
    {
        if (token_varify(ter.name!!))
        {
            var user = userRepo.getByToken(ter.name!!)
            var animal = animalRepo.getByAnimal(id.toLong())
            var order = orderRepo.getOrder(user!!.id_user!!,id.toLong())
            var detail = orderdetail(order.id_order.toString(),order.animal_name!!,order.animal_pic!!,order.animal_price!!,order.animal_rating.toString(),animal[0].animal_location!!,order.order_id_tracking!!,order.order_location!!,order.order_status!!,order.order_time!!,(order.animal_price.toString().toInt()*0.20).toString())
            return detail
        }
        else
        {
            var kn = orderdetail("no data","no data","no data","no data","no data","no data","no data","no data","no data","no data","no data")
            return kn
        }
    }

    @PostMapping("/orderdetail/update/{id}")
    fun orderupdate(@RequestBody upd:update):ret
    {
        if (token_varify(upd.token))
        {
            var ord = orderRepo.findById(upd.fullname.toLong())
            var order = ord.get()
            order.order_location = upd.address
            orderRepo.save(order)
            var kn = ret("Updated")
            return kn
        }
        else
        {
            var kn = ret("no token")
            return kn
        }
    }

    @GetMapping("/sinron/delete/{id}")
    fun deleteproduct(@PathVariable id:String):ret
    {
        var product = animalRepo.findById(id.toLong())
        if (product.isPresent)
        {
            var data = product.get()
            fileStorage.delete(data.animal_pic!!)
            var animals = imageRepo.getByImage(data.id_animal!!)
            animals.forEach {
                fileStorage.delete(it.image_url!!)
                imageRepo.deleteById(it.id_animal_image!!)
            }

            var wish = wishRepo.getWishList(data.id_animal!!)
            wish.forEach {
                wishRepo.deleteById(it.id_wish!!)
            }

            var cart = cartRepo.getCartlists(data.id_animal!!)
            cart.forEach {
                cartRepo.deleteById(it.id_cart!!)
            }

            animalRepo.deleteById(data.id_animal!!)

            var kn = ret(data.animal_pic)
            return kn
        }
        else
        {
            var kn = ret("no animal")
            return kn
        }
    }

    @GetMapping("/sinron/pending/delete/{id}")
    fun pending_delete(@PathVariable id:String):ret
    {
        orderRepo.deleteById(id.toLong())
        var kn = ret("Deleted")
        return kn
    }

    @GetMapping("/sinron/order/delete/{id}")
    fun confirm_delete(@PathVariable id:String):ret
    {
        delivertRepo.deleteById(id.toLong())
        var kn = ret("Deleted")
        return kn
    }

    @GetMapping("/sinron/tracking/{id}/{status}")
    fun statusupdate(@PathVariable id:String, @PathVariable status:String):ret
    {
        var deliver = delivertRepo.findById(id.toLong())//.getByOrderId(id.toLong())
        var del =  deliver.get()
        del.estimated_time = LocalDateTime.of(2020,6,28,10,10,10)//status+" Hours"
        delivertRepo.save(del)
        var kn = ret("updated to ${status} hours")
        return kn
    }

    @PostMapping("/cart/available/{id}")
    fun availability(@RequestBody ter:ret, @PathVariable id : String): MutableList<Cart>
    {
        if (token_varify(ter.name!!))
        {
            var usr = userRepo.getByToken(ter.name!!)
            var user = cartRepo.getCartlist(usr!!.id_user!!)
            return user
        }
        else
        {
            return Collections.emptyList()
        }
    }

    @PostMapping("/purchase")
    fun purchase(@RequestBody prchs:purchase):ret
    {
        if (token_varify(prchs.token))
        {
            var user = userRepo.getByToken(prchs.token)
            var cart = cartRepo.getCart(prchs.product_id.toLong(),user!!.id_user!!.toLong())
            var k1 = orderRepo.save(Order(0,user.id_user,cart.animal_id,user.name,prchs.address,user.phone,prchs.phone,cart.animal_name,cart.animal_des,cart.animal_price,"Pending",cart.animal_rating,cart.animal_pic,LocalDateTime.now().dayOfMonth.toString()+"-"+LocalDateTime.now().month.toString()+"-"+LocalDateTime.now().year.toString(),"", prchs.address))
            var ord = orderRepo.findById(k1.id_order!!)
            var data = ord.get()
            data.order_id_tracking = data.id_order.toString()+data.user_id.toString()+data.animal_id.toString()
            orderRepo.save(data)
            cartRepo.deleteById(cart.id_cart!!)
            var kn = ret("Ordered")
            return kn
        }
        else
        {
            var kn = ret("no token")
            return kn
        }
    }

    @PostMapping("/access/{id}")
    fun access(@RequestBody ter:ret, @PathVariable id :String):ret
    {
        if (token_varify(ter.name!!))
        {
            var user = userRepo.getByToken(ter.name)
            try {
                var delvry = delivertRepo.getDeliveryAvail(id.toLong(), user!!.id_user!!.toLong())
                if (delvry.size<=0)
                {
                    var ord = orderRepo.getOrderAvail(id.toLong(), user.id_user!!.toLong())
                    if (ord.size<=0)
                    {
                        var crt = cartRepo.getCartAvail(id.toLong(),user.id_user!!.toLong())
                        if (crt.size<=0)
                        {
                            var trt = ret("no")
                            return trt
                        }
                        else
                        {
                            var trt = ret("yes")
                            return trt
                        }
                    }
                    else
                    {
                        var trt = ret("yes")
                        return trt
                    }
                }
                else
                {
                    var trt = ret("yes")
                    return trt
                }
            }catch (e:Exception)
            {
                var trt = ret("yes")
                return trt
            }

        }
        else
        {
            var kn = ret("no token")
            return kn
        }
    }


    @GetMapping("/delete_test/{id}")
    fun deleteTest(@PathVariable id:String)
    {
        fileStorage.delete(id)
    }

    @GetMapping("/sinthy/{id}")
    fun sinthy(@PathVariable id:String):ret
    {
        var kn = ret(id)
        return kn
    }


    fun token_varify(token:String):Boolean
    {
        val k = userRepo.getByToken(token)

        try {
            return  !k!!.phone.toString().isEmpty()
        }catch (e:Exception)
        {
            return false
        }
    }

    fun token(number:String):String
    {
        var tk = LocalDateTime.now().toString()
        tk = tk.replace(",","")
        tk = tk.replace(".","")
        tk = tk.replace(":","")
        tk = tk.replace("-","")
        tk = tk+number+tk
        return tk
    }

}