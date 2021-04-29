package coms309.people;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.HashMap;

/**
 * 
 * Creating my own simpler controller for bands
 * 
 * @author Cutler Thayer
 *
 */
@RestController
public class BandController {

	HashMap<String, Band> bandList = new  HashMap<>();
	
	@GetMapping("/bands")
	public @ResponseBody HashMap<String, Band> getAllBands(){
		return bandList;
	}
	
	@PostMapping("/bands")
	public @ResponseBody String createBand(@RequestBody Band band){
		System.out.println(band);
		bandList.put(band.getName(), band);
		return "New band "+ band.getName() + " Saved";
	}
	
	@GetMapping("/bands/{name}/concert")
	public @ResponseBody String bandConcert(@PathVariable String name) {
		Band b = bandList.get(name);
		return b.concertString();
	}
	
	@DeleteMapping("/bands/{name}")
	public @ResponseBody HashMap<String, Band> deleteBand(@PathVariable String name) {
		bandList.remove(name);
		return bandList;
	}
}