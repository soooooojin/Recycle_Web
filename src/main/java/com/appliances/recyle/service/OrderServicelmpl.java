package com.appliances.recyle.service;

import com.appliances.recyle.domain.Item;
import com.appliances.recyle.domain.Member;
import com.appliances.recyle.domain.Order;
import com.appliances.recyle.domain.Pay;
import com.appliances.recyle.dto.OrderDTO;
import com.appliances.recyle.dto.OrderItemDTO;
import com.appliances.recyle.repository.ItemRepository;
import com.appliances.recyle.repository.MemberRepository;
import com.appliances.recyle.repository.OrderRepository;
import com.appliances.recyle.repository.PayRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class OrderServicelmpl implements OrderService {

    @Autowired
    private PayRepository payRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    // @Autowired ::final:더 이상 바꾸지 않음
    //    private final TypeRepository typeRepository;

    @Autowired
    public OrderServicelmpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
    //  this.payRepository = payRepository;
    }

    @Override
    public List<OrderDTO> getOrders() {
        return orderRepository.findAll().stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO read(Long ono) {
        return orderRepository.findById(ono)
                .map(this::entityToDTO)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public OrderDTO entityToDTO(Order order) {

        OrderDTO orderDTO = OrderDTO.builder()
                .ono(order.getOno())
                //           memberdb것
                .email(order.getMember().getEmail())
                .iname(order .getItem().getIname())
                .iprice(order.getItem().getIprice())
                .purl(order.getPurl())
                .oaddress(order.getOaddress())
                .pmethod(order.getPay().getPmethod())
                .build();

//        List<String> images = order.getPurl().stream()
//                .map(image -> image.get);
// 사진올라가는게 여긴가?

        return orderDTO;
    }


    // 있어야할까? 히지가 필요할지도?
    @Override
    public List<OrderDTO> searchOrders(String keyword) {
        return List.of();
    }

    @Override
    public Long insert(OrderDTO orderDTO) {
        Order order = dtoTOEntity(orderDTO);
        Long ono = orderRepository.save(order).getOno();
        return ono;
    }

    @Override
    public Order dtoTOEntity(OrderDTO orderDTO) {
        //등록할 때 이름으로 하나? -> 무조건 iname!
//        Item item = itemRepository.findByItem(orderDTO.getIname(),orderDTO.getIprice())
//                .orElseGet(() -> {
//                    Item newitem = Item.builder()
//                            .iname(orderDTO.getIname()) //제품이름
//                            .iprice(orderDTO.getIprice()) //제품 스티커 가격
//                            .build();
//
//                    return itemRepository.save(newitem);
//                });

        //Pay도 들어가야 함!
        // Pay pay =


        //member
        Member member = memberRepository.findByEmail(orderDTO.getEmail())
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .email(orderDTO.getEmail())
                            .build();

                    return newMember;
                });

        Order order = Order.builder()
                .ono(orderDTO.getOno())
                .member(member)
//                .item(item)
//               .pay(pay) -> pay만들면 추가하기!
                .purl(orderDTO.getPurl())
                .ostatus(orderDTO.getOstatus())
                .oaddress(orderDTO.getOaddress())
                .build();


        return order;
    }

    @Override
    public void update(OrderDTO orderDTO) {
        // DTO를 Entity로 변환
        Order order = dtoTOEntity(orderDTO);
        // orderRepository 통해 저장된 Entity를 업데이트
        orderRepository.save(order);
    }

    @Override
    public void delete(OrderDTO orderDTO) {
        orderRepository.deleteById(orderDTO.getOno());
    }

    @Transactional
    public void saveOrder(String email, OrderItemDTO orderItemDTO) {
        // 이메일로 Member 찾기
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // Item을 iname으로 조회
        Item item = itemRepository.findByIname(orderItemDTO.getIname())
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + orderItemDTO.getIname()));

        // Pay 객체 생성
        Pay pay = Pay.builder()
                .pmethod("아직 미정")  // 기본 결제 방식 설정
                .pstatus("진행 중")    // 기본 결제 상태 설정
                .amount(null)          // 금액은 null로 설정
                .pdate(null)           // 결제 날짜는 null로 설정
                .build();

        // Pay 객체를 먼저 저장 (영속화)
        pay = payRepository.save(pay);  // payRepository를 사용하여 Pay 객체 저장

        // Order 생성 후 저장
        Order order = Order.builder()
                .member(member) // Member 객체
                .item(item) // Item 객체
                .pay(pay)
                .purl(orderItemDTO.getPurl()) // 이미지 경로
                .ostatus("진행 중") // 진행 상태
                .oaddress(orderItemDTO.getOaddress()) // 주소
                .odate(orderItemDTO.getOdate())
                .build();

        orderRepository.save(order);
    }

}
