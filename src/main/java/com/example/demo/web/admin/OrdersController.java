//受注関連のコントローラ
package com.example.demo.web.admin;

import java.util.List;

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
import com.example.demo.service.BaseService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/orders")
public class OrdersController {
	@Autowired
	BaseService<Order> orderService;

	/*
	 * 一覧表示
	 */
	@GetMapping(path = {"", "/"})
	public String list(Model model) {
		// 全件取得
		List<Order> orders = orderService.findAll();
		model.addAttribute("orders", orders);
		return "admin/orders/list";
	}
	
	/*
	 * 新規作成画面表示
	 */
	@GetMapping(value = "/create")
	public String form(Order order, Model model) {
		model.addAttribute("order", order);
		return "admin/orders/create";
	}

	/*
	 * 新規登録
	 */
	@PostMapping(value = "/create")
	public String register(@Valid Order order, BindingResult result, Model model, RedirectAttributes ra) {
		FlashData flash;
		try {
			if (result.hasErrors()) {
				return "admin/orders/create";
			}
			// 新規登録
			orderService.save(order);
			flash = new FlashData().success("新規作成しました");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin/orders";
	}
	
	/*
	 * 受注表示
	 */
	@GetMapping(value = "/view/{id}")
	public String list1(Model model, @PathVariable Integer id) throws DataNotFoundException {
		// idから取得
		Order order = orderService.findById(id);
		model.addAttribute("order", order);
//		List<OrderDetail> orderdetails = orderDetailService.findAll();
//		OrderDetail orderdetails = orderDetailService.findById(id);
//		model.addAttribute("orderdetails", orderdetails);
		return "admin/orders/view";
	}
	
	/*
	 * 受注編集画面表示
	 */
	@GetMapping(value = "/edit/{id}")
	public String edit(Model model, @PathVariable Integer id, RedirectAttributes ra) throws DataNotFoundException {
		try {
			// 存在確認
			Order order = orderService.findById(id);
			model.addAttribute("order", order);
		} catch (Exception e) {
			FlashData flash = new FlashData().danger("該当データがありません");
			ra.addFlashAttribute("flash", flash);
			return "redirect:/admin/orders";
		}
		return "admin/orders/edit";
	}
	
	/*
	 * 更新
	 */
	@PostMapping(value = "/edit/{id}")
	public String update(@PathVariable Integer id, @Valid Order order, BindingResult result, Model model, RedirectAttributes ra) {
		FlashData flash;
		try {
			if (result.hasErrors()) {
				return "admin/orders/edit"; //もう一度編集画面を表示
			}
			orderService.findById(id);
			// 更新
			orderService.save(order);
			flash = new FlashData().success("更新しました");
		} catch (Exception e) {
			flash = new FlashData().danger("該当データがありません");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin/orders/view/{id}";
	}

}
