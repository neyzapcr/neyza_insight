package com.example.neyza_insight.Home.pertemuan_10

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neyza_insight.R
import com.example.neyza_insight.databinding.FragmentTabKelahiranBinding


class TabKelahiranFragment : Fragment() {
    private var _binding: FragmentTabKelahiranBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTabKelahiranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listData = listOf(
            KelahiranModel(
                "Anggabaya Unggul Haryanto S.E.",
                "AKTA-832819",
                "04/12/2026",
                "Jakarta Barat",
                "Raihan Nababan",
                "Nabila Nuraini",
                "https://randomuser.me/api/portraits/men/1.jpg"
            ),
            KelahiranModel(
                "Siti Aminah",
                "AKTA-110293",
                "15/01/2026",
                "Pekanbaru",
                "Budi Santoso",
                "Siti Khadijah",
                "https://randomuser.me/api/portraits/women/2.jpg"
            ),
            KelahiranModel(
                "Aris Setiawan",
                "AKTA-110294",
                "22/01/2026",
                "Surabaya",
                "Joko Susilo",
                "Rini Ambarwati",
                "https://randomuser.me/api/portraits/men/3.jpg"
            ),
            KelahiranModel(
                "Dewi Lestari",
                "AKTA-110295",
                "05/02/2026",
                "Bandung",
                "Asep Sunandar",
                "Euis Dahlia",
                "https://randomuser.me/api/portraits/women/4.jpg"
            ),
            KelahiranModel(
                "Rian Hidayat",
                "AKTA-110296",
                "14/02/2026",
                "Medan",
                "Sitorus Pane",
                "Murniati",
                "https://randomuser.me/api/portraits/men/5.jpg"
            ),
            KelahiranModel(
                "Slamet Rahardjo",
                "AKTA-110297",
                "03/03/2026",
                "Yogyakarta",
                "Bambang Tri",
                "Sri Wahyuni",
                "https://randomuser.me/api/portraits/men/6.jpg"
            ),
            KelahiranModel(
                "Putu Ayu Saraswati",
                "AKTA-110298",
                "19/03/2026",
                "Denpasar",
                "I Wayan Sudarta",
                "Ni Luh Ketut",
                "https://randomuser.me/api/portraits/women/7.jpg"
            ),
            KelahiranModel(
                "Muhammad Rizky",
                "AKTA-110299",
                "28/03/2026",
                "Makassar",
                "Andi Ahmad",
                "Siti Fatimah",
                "https://randomuser.me/api/portraits/men/8.jpg"
            ),
            KelahiranModel(
                "Aditya Pratama",
                "AKTA-110300",
                "09/04/2026",
                "Semarang",
                "Dedi Wijaya",
                "Retno Palupi",
                "https://randomuser.me/api/portraits/men/9.jpg"
            ),
            KelahiranModel(
                "Fitri Handayani",
                "AKTA-110301",
                "21/04/2026",
                "Palembang",
                "Hasan Basri",
                "Rusmini",
                "https://randomuser.me/api/portraits/women/10.jpg"
            ),
            KelahiranModel(
                "Budi Utomo",
                "AKTA-110302",
                "02/05/2026",
                "Malang",
                "Agus Setiyono",
                "Endang Sri",
                "https://randomuser.me/api/portraits/men/11.jpg"
            ),
            KelahiranModel(
                "Nanda Saputra",
                "AKTA-110303",
                "11/05/2026",
                "Banjarmasin",
                "Hermanudin",
                "Siti Rahmah",
                "https://randomuser.me/api/portraits/men/12.jpg"
            ),
            KelahiranModel(
                "Citra Kirana",
                "AKTA-110304",
                "25/05/2026",
                "Bogor",
                "Cecep Rahman",
                "Neng Hasanah",
                "https://randomuser.me/api/portraits/women/13.jpg"
            ),
            KelahiranModel(
                "Dimas Anggara",
                "AKTA-110305",
                "04/06/2026",
                "Solo",
                "Triyono",
                "Siti Lestari",
                "https://randomuser.me/api/portraits/men/14.jpg"
            ),
            KelahiranModel(
                "Aulia Rahma",
                "AKTA-110306",
                "18/06/2026",
                "Padang",
                "Syamsul Bahri",
                "Ernawati",
                "https://randomuser.me/api/portraits/women/15.jpg"
            ),
            KelahiranModel(
                "Eko Prasetyo",
                "AKTA-110307",
                "01/07/2026",
                "Balikpapan",
                "Rudi Hartono",
                "Yulianti",
                "https://randomuser.me/api/portraits/men/16.jpg"
            ),
            KelahiranModel(
                "Indah Permatasari",
                "AKTA-110308",
                "12/07/2026",
                "Manado",
                "Frans Wowor",
                "Maria Lengkey",
                "https://randomuser.me/api/portraits/women/17.jpg"
            ),
            KelahiranModel(
                "Gilang Dirga",
                "AKTA-110309",
                "29/07/2026",
                "Jakarta Selatan",
                "Wendy",
                "Siska",
                "https://randomuser.me/api/portraits/men/18.jpg"
            ),
            KelahiranModel(
                "Lesti Kejora",
                "AKTA-110310",
                "08/08/2026",
                "Cianjur",
                "Endang Mulyana",
                "Sukmawati",
                "https://randomuser.me/api/portraits/women/19.jpg"
            ),
            KelahiranModel(
                "Rizky Billar",
                "AKTA-110311",
                "17/08/2026",
                "Medan",
                "Daniel",
                "Rosmala",
                "https://randomuser.me/api/portraits/men/20.jpg"
            ),
            KelahiranModel(
                "Hendra Setiawan",
                "AKTA-110312",
                "03/09/2026",
                "Jambi",
                "Agustinus",
                "Theresia",
                "https://randomuser.me/api/portraits/men/21.jpg"
            ),
            KelahiranModel(
                "Mega Utami",
                "AKTA-110313",
                "15/09/2026",
                "Pontianak",
                "Tan Wijaya",
                "Meilani",
                "https://randomuser.me/api/portraits/women/22.jpg"
            ),
            KelahiranModel(
                "Taufik Hidayat",
                "AKTA-110314",
                "27/09/2026",
                "Bandung",
                "Ahmad Haris",
                "Enung",
                "https://randomuser.me/api/portraits/men/23.jpg"
            ),
            KelahiranModel(
                "Yuni Shara",
                "AKTA-110315",
                "10/10/2026",
                "Batu",
                "Trenggono",
                "Rachma Widadiningsih",
                "https://randomuser.me/api/portraits/women/24.jpg"
            ),
            KelahiranModel(
                "Krisdayanti",
                "AKTA-110316",
                "24/10/2026",
                "Batu",
                "Trenggono",
                "Rachma Widadiningsih",
                "https://randomuser.me/api/portraits/women/25.jpg"
            ),
            KelahiranModel(
                "Anang Hermansyah",
                "AKTA-110317",
                "05/11/2026",
                "Jember",
                "Cholib",
                "Anissa",
                "https://randomuser.me/api/portraits/men/26.jpg"
            ),
            KelahiranModel(
                "Ashanty Siddik",
                "AKTA-110318",
                "16/11/2026",
                "Jakarta",
                "Soejahjo Hasnoputro",
                "Farida",
                "https://randomuser.me/api/portraits/women/27.jpg"
            ),
            KelahiranModel(
                "Aurel Hermansyah",
                "AKTA-110319",
                "30/11/2026",
                "Jakarta Pusat",
                "Anang Hermansyah",
                "Krisdayanti",
                "https://randomuser.me/api/portraits/women/28.jpg"
            ),
            KelahiranModel(
                "Atta Halilintar",
                "AKTA-110320",
                "11/12/2026",
                "Dumai",
                "Halilintar Anofial",
                "Lenggogeni Faruk",
                "https://randomuser.me/api/portraits/men/29.jpg"
            ),
            KelahiranModel(
                "Thariq Halilintar",
                "AKTA-110321",
                "25/12/2026",
                "Brunei",
                "Halilintar Anofial",
                "Lenggogeni Faruk",
                "https://randomuser.me/api/portraits/men/30.jpg"
            )
        )

        binding.rvKelahiran.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = KelahiranAdapter(listData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}