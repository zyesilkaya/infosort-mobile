package com.example.deneme1.waiter

class Orders {
 /*   val orders = MutableLiveData<MutableList<Order>>()

    init {
        orders.value = mutableListOf()
    }



    fun deleteOrderWithIndex(index:Int, state:Int){
        orders.value?.let { deleteOrder(it[index], state) }

    }

    fun deleteOrder(parseObject: Order, state:Int){
        parseObject.deleteInBackground { e ->
            if (e == null) {
                if(state==0){
                    readOrder()
                }else{
                    readOwnedOrder()
                }
            }else {
            }
        }

    }

    fun ownedWithIndex(index:Int, state: Int){
        orders.value?.let { it[index].owner= ParseUser.getCurrentUser().username
            addOrUpdateOrder(it[index],state )}
    }

    fun doWithIndex(index:Int, state: Int){
        if(orders.value?.get(index)?.owner == null){
            ownedWithIndex(index, state)
        }else{
            deleteOrderWithIndex(index,state)
        }

    }

    fun doWithObject(parseObject: Order, state: Int){
        if(parseObject.owner == null){
            orders.value?.let { parseObject.owner= ParseUser.getCurrentUser().username
                addOrUpdateOrder(parseObject,state )}
        }else{
            orders.value?.let { deleteOrder(parseObject, state) }
        }

    }

*/
}