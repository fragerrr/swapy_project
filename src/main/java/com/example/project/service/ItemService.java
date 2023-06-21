package com.example.project.service;

import com.example.project.models.Item;
import com.example.project.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findByClientId(Integer id){
        return itemRepository.findByOwnerId(id);
    }

    public List<Item> findAll(){
        return itemRepository.findAll();
    }

    public Item findById(Integer id){
        return itemRepository.findById(id).orElse(null);
    }
    public void save(Item item){
        itemRepository.save(item);
    }

    public List<Item> findLast(){
        return itemRepository.findAll(PageRequest.of(0, 10)).getContent();
    }

    public void delete(Integer id){
        Optional<Item> item = itemRepository.findById(id);

        item.ifPresent(itemRepository::delete);
    }
}
