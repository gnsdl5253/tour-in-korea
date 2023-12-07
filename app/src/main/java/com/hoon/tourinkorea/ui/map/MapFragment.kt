package com.hoon.tourinkorea.ui.map

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoon.tourinkorea.databinding.FragmentMapBinding
import com.hoon.tourinkorea.ui.write.WriteActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment(), MapView.POIItemEventListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val listItems = mutableListOf<Place>()
    private lateinit var mapAdapter: MapAdapter
    private lateinit var mapView: MapView
    private val viewModel: MapViewModel by viewModels()

    @Inject
    lateinit var kakaoApiClient: KakaoApiClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeMapView()
        setupSearchButton()
        initializeRecyclerView()

        mapView.setPOIItemEventListener(this)
    }

    private fun initializeMapView() {
        mapView = MapView(requireActivity())
        mapAdapter = MapAdapter(object : MapItemClickListener {
            override fun onClick(place: Place) {
                val mapPoint = MapPoint.mapPointWithGeoCoord(place.y, place.x)
                mapView.setMapCenterPointAndZoomLevel(mapPoint, 1, true)
                binding.rvMapSearch.visibility = View.GONE
            }
        })

        binding.kakaoMapView.addView(mapView)
    }

    private fun initializeRecyclerView() {
        binding.rvMapSearch.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvMapSearch.adapter = mapAdapter

        binding.etSearchField.bringToFront()

        binding.etSearchField.setOnClickListener {
            val keyword = binding.etSearchField.text.toString()
            if (keyword.isNotEmpty()) {
                binding.rvMapSearch.bringToFront()
                binding.rvMapSearch.visibility = View.VISIBLE
                searchKeyword(keyword)
            } else {
                binding.rvMapSearch.visibility = View.GONE
            }
        }

        binding.etSearchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString()
                if (keyword.isNotEmpty()) {
                    binding.rvMapSearch.bringToFront()
                    binding.rvMapSearch.visibility = View.VISIBLE
                    searchKeyword(keyword)
                } else {
                    binding.rvMapSearch.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupSearchButton() {
        binding.btnSearch.setOnClickListener {
            val keyword = binding.etSearchField.text.toString()
            binding.rvMapSearch.bringToFront()
            searchKeyword(keyword)
            Log.d("MapFragment", "Search button clicked with keyword: $keyword")
        }
    }

    private fun searchKeyword(keyword: String) {

        lifecycleScope.launch {
            viewModel.searchKeyword(keyword)
            viewModel.items.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { places ->
                    Log.d("MapFragment", "collect block executed")
                    addItemsAndMarkers(places)
                }
        }
    }

    private fun addItemsAndMarkers(searchResult: ResultSearchKeyword?) {
        if (!searchResult?.documents.isNullOrEmpty()) {
            Log.d("MapFragment", "Search result is not empty")
            listItems.clear()
            mapView.removeAllPOIItems()
            for (document in searchResult!!.documents) {
                val item = Place(
                    document.place_name,
                    document.road_address_name,
                    document.address_name,
                    document.x,
                    document.y
                )
                listItems.add(item)

                val point = MapPOIItem()
                point.apply {
                    itemName = document.place_name
                    mapPoint = MapPoint.mapPointWithGeoCoord(
                        document.y,
                        document.x
                    )
                    markerType = MapPOIItem.MarkerType.BluePin
                    selectedMarkerType = MapPOIItem.MarkerType.RedPin

                    userObject = document.road_address_name
                }
                mapView.addPOIItem(point)
            }
            binding.rvMapSearch.visibility = View.VISIBLE
            mapAdapter.submitList(listItems.toMutableList())

        } else {
            Log.d("MapFragment", "Search result is empty")
            binding.rvMapSearch.visibility = View.GONE
        }
    }
    override fun onResume() {
        super.onResume()
        binding.etSearchField.text = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {}

    override fun onCalloutBalloonOfPOIItemTouched(
        mapView: MapView?,
        poiItem: MapPOIItem?,
        buttonType: MapPOIItem.CalloutBalloonButtonType?,
    ) {
        val builder = AlertDialog.Builder(requireContext())
        val itemList = arrayOf("글 작성", "취소")
        builder.setTitle("${poiItem?.itemName}")
        builder.setItems(itemList) { dialog, which ->
            when (which) {
                0 -> {
                    val intent = Intent(requireContext(), WriteActivity::class.java)
                    val roadAddressName = poiItem?.userObject as? String ?: "Unknown Road Address"

                    intent.putExtra("roadAddressName", roadAddressName)
                    Log.d("MapFragment", "Selected roadAddressName: $roadAddressName")
                    startActivity(intent)
                }

                1 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {}

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {}
}