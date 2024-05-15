package com.example.demo.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.common.DataNotFoundException;
import com.example.demo.common.FlashData;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.service.BaseService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/orderdetails")
public class OrderDetailsController {
	@Autowired
	BaseService<OrderDetail> orderDetailService;
	@Autowired
	BaseService<Order> orderService;

	/*
	 * 新規作成画面表示
	 */
	@GetMapping(value = "/create/{id}")
	public String form(@PathVariable Integer id, OrderDetail orderDetail, Model model, RedirectAttributes ra)throws DataNotFoundException {
		try {
			//OrderDetail orderdetail = orderDetailService.findById(id);
			model.addAttribute("orderDetail", orderDetail); //orderdetailの情報をビュー側に送りたい
		}catch (Exception e) {
			FlashData flash = new FlashData().danger("該当データがありません");
			ra.addFlashAttribute("flash", flash);
			return "redirect:/admin/orders";
		}
		return "admin/orderdetails/create";
	}
	
	/*
	 * 新規登録
	 */
	@PostMapping(value = "/create/{order_id}")
	public String register(@PathVariable Integer order_id, @Valid OrderDetail orderDetail, BindingResult result, Model model, RedirectAttributes ra) {
		FlashData flash;
		try {
			if (result.hasErrors()) {
				return "admin/orderdetails/create";
			}
			Order order = orderService.findById(order_id);
//			Order order = new Order();
//			order.setId(order_id);
			orderDetail.setOrder(order);
			// 新規登録
			orderDetailService.save(orderDetail);
			flash = new FlashData().success("新規作成しました");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin/orders/view/" + order_id;
	}
	
	/*
	 * 受注編集画面表示
	 */
	@GetMapping(value = "/edit/{id}")
	public String edit(Model model, @PathVariable Integer id, RedirectAttributes ra) throws DataNotFoundException {
		try {
			// 存在確認
			OrderDetail orderDetail = orderDetailService.findById(id);
			model.addAttribute("orderDetail", orderDetail);
		} catch (Exception e) {
			FlashData flash = new FlashData().danger("該当データがありません");
			ra.addFlashAttribute("flash", flash);
			return "redirect:/admin/orders";
		}
		return "admin/orderdetails/edit";
	}
	
	/*
	 * 更新
	 */
	@PostMapping(value = "/edit/{id}")
	public String update(@PathVariable Integer id, @Valid OrderDetail orderDetail, BindingResult result, Model model, RedirectAttributes ra) {
		FlashData flash;
		Integer order_id = orderDetail.getOrder().getId();
		try {
			if (result.hasErrors()) {
				return "admin/orderdetails/edit"; //もう一度編集画面を表示
			}
//			Order order = orderService.findById(id);
//			orderdetail.setOrder(order);
			// 更新	
			orderDetailService.save(orderDetail);
			flash = new FlashData().success("更新しました");
		} catch (Exception e) {
			flash = new FlashData().danger("該当データがありません");
		}
		ra.addFlashAttribute("flash", flash);
		
		return "redirect:/admin/orders/view/" + order_id;
	}

}
