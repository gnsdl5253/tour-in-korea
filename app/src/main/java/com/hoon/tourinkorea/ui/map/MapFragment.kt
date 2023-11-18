package com.hoon.tourinkorea.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoon.tourinkorea.BuildConfig
import com.hoon.tourinkorea.databinding.FragmentMapBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@AndroidEntryPoint
class MapFragment : Fragment()  {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val listItems = arrayListOf<Place>()
    private val mapAdapter = MapAdapter(listItems)
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mapView = MapView(requireActivity())
        binding.kakaoMapView.addView(mapView)

        binding.rvMapSearch.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvMapSearch.adapter = mapAdapter
        mapAdapter.setItemClickListener(object: MapAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val mapPoint =
                    MapPoint.mapPointWithGeoCoord(listItems[position].y, listItems[position].x)
                mapView.setMapCenterPointAndZoomLevel(mapPoint, 1, true)
                binding.rvMapSearch.visibility = View.GONE
            }
        })

        binding.etSearchField.bringToFront()

        binding.btnSearch.setOnClickListener{
            val keyword = binding.etSearchField.text.toString()
            binding.rvMapSearch.visibility = View.VISIBLE
            binding.rvMapSearch.bringToFront()
            searchKeyword(keyword)
        }
    }

    private fun searchKeyword(keyword: String) {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.KAKAO_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val api = retrofit.create(KakaoApiClient::class.java)
        val call = api.getSearchKeyword(BuildConfig.API_KEY, keyword)

        call.enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(
                call: Call<ResultSearchKeyword>,
                response: Response<ResultSearchKeyword>,
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("MapFragment", "Received data: $result")
                    addItemsAndMarkers(result)
                } else {
                    Log.e("MapFragment", "Request failed with code: ${response.code()}")
                    binding.rvMapSearch.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                Log.e("MapFragment", "Request failed with exception: ${t.message}", t)
            }
        })
    }

    private fun addItemsAndMarkers(searchResult: ResultSearchKeyword?) {
        if (!searchResult?.documents.isNullOrEmpty()) {
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
                    mapPoint = MapPoint.mapPointWithGeoCoord(document.y,
                        document.x)
                    markerType = MapPOIItem.MarkerType.BluePin
                    selectedMarkerType = MapPOIItem.MarkerType.RedPin
                }
                mapView.addPOIItem(point)
            }
            mapAdapter.notifyDataSetChanged()

        } else {
            Toast.makeText(context, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
            binding.rvMapSearch.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
