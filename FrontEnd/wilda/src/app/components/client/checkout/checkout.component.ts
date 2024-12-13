import { Component, OnInit } from '@angular/core';
import { faBars, faHeart, faPhone, faShoppingBag } from '@fortawesome/free-solid-svg-icons';
import { MessageService } from 'primeng/api';
import { Order } from 'src/app/_class/order';
import { OrderDetail } from 'src/app/_class/order-detail';
import { HttpClient } from '@angular/common/http';
import { CartService } from 'src/app/_service/cart.service';
import { OrderService } from 'src/app/_service/order.service';
import { StorageService } from 'src/app/_service/storage.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css'],
  providers: [MessageService]
})
export class CheckoutComponent implements OnInit {
  heart = faHeart;
  bag = faShoppingBag;
  phone = faPhone;
  bars = faBars;
  showDepartment = false;
  order = new Order();
  listOrderDetail: any[] = [];
  username!: string;

  orderForm: any = {
    firstname: null,
    lastname: null,
    country: null,
    address: null,
    town: null,
    state: null,
    postCode: null,
    email: null,
    phone: null,
    note: null
  };

  constructor(
    public cartService: CartService,
    private orderService: OrderService,
    private storageService: StorageService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    console.log('[ngOnInit] Initializing Checkout Component');
    this.username = this.storageService.getUser().username;
    console.log(`[ngOnInit] Username: ${this.username}`);
    this.cartService.getItems();
    console.log('[ngOnInit] Cart items fetched:', this.cartService.items);
  }

  showDepartmentClick() {
    this.showDepartment = !this.showDepartment;
    console.log(`[showDepartmentClick] Toggled department display: ${this.showDepartment}`);
  }

  placeOrder() {
    console.log('[placeOrder] Starting order placement');
    this.listOrderDetail = []; // Reset list before pushing new items
    this.cartService.items.forEach(res => {
      let orderDetail: OrderDetail = new OrderDetail();
      orderDetail.name = res.name;
      orderDetail.price = res.price;
      orderDetail.quantity = res.quantity;
      orderDetail.subTotal = res.subTotal;
      this.listOrderDetail.push(orderDetail);
      console.log('[placeOrder] Added order detail:', orderDetail);
    });

    const { firstname, lastname, country, address, town, state, postCode, phone, email, note } = this.orderForm;
    console.log('[placeOrder] Order Form Data:', this.orderForm);
    console.log('[placeOrder] Username:', this.username);

    this.orderService
      .placeOrder(
        firstname,
        lastname,
        country,
        address,
        town,
        state,
        postCode,
        phone,
        email,
        note,
        this.listOrderDetail,
        this.username
      )
      .subscribe({
        next: res => {
          console.log('[placeOrder] Order placed successfully:', res);
          this.cartService.clearCart();
          console.log('[placeOrder] Cart cleared');

          const totalAmount = this.listOrderDetail.reduce((sum, item) => sum + item.subTotal, 0);
          console.log('[placeOrder] Total order amount:', totalAmount);

          const paymentPayload = {
            amount: totalAmount.toString(),
            orderInfo: `Đơn hàng ${firstname} ${phone}`
          };
          console.log('[placeOrder] Payment Payload:', paymentPayload);

          this.http
            .post<any>('http://localhost:8080/api/payment/create', paymentPayload, {
              headers: { 'Content-Type': 'application/json' }
            })
            .subscribe({
              next: (paymentResponse: any) => {
                console.log('[placeOrder] Payment API response:', paymentResponse);
                const payUrl = paymentResponse.payUrl;
                if (payUrl) {
                  console.log('[placeOrder] Redirecting to payment URL:', payUrl);
                  window.location.href = payUrl;
                }
              },
              error: (error: any) => {
                console.error('[placeOrder] Payment API error:', error);
              }
            });
        },
        error: err => {
          console.error('[placeOrder] Order placement error:', err);
        }
      });
  }
}
