package com.example.neyza_insight.Home.pertemuan_10

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neyza_insight.R
import com.example.neyza_insight.databinding.FragmentTabKematianBinding

class TabKematianFragment : Fragment() {
    private var _binding: FragmentTabKematianBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTabKematianBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listData = listOf(
            KematianModel(
                "Gibran Erlangga",
                "1471020039480",
                "Laki-laki",
                "20/03/2026",
                "RSUD Arifin Achmad",
                "Sakit Tua",
                "SKM-92819",
                "Jl. Sudirman No. 45",
                "https://randomuser.me/api/portraits/men/31.jpg"
            ),
            KematianModel(
                "Dewi Lestari",
                "1471092830193",
                "Perempuan",
                "02/05/2026",
                "Rumah Kediaman",
                "Serangan Jantung",
                "SKM-10293",
                "Jl. Panam Blok C",
                "https://randomuser.me/api/portraits/women/32.jpg"
            ),
            KematianModel(
                "Bambang Sulistyo",
                "147101020348001",
                "Laki-laki",
                "12/01/2026",
                "RSUD Arifin Achmad",
                "Stroke",
                "SKM-92820",
                "Jl. Tuanku Tambusai No. 12",
                "https://randomuser.me/api/portraits/men/33.jpg"
            ),
            KematianModel(
                "Siti Rahmah",
                "147105421093002",
                "Perempuan",
                "18/01/2026",
                "Rumah Kediaman",
                "Sakit Tua",
                "SKM-92821",
                "Jl. Soebrantas Samping Ponpes",
                "https://randomuser.me/api/portraits/women/34.jpg"
            ),
            KematianModel(
                "Ahmad Hidayat",
                "147103120572003",
                "Laki-laki",
                "02/02/2026",
                "RS Jiwa Tampan",
                "Komplikasi",
                "SKM-92822",
                "Jl. Suka Karya Gg. Al-Ikhlas",
                "https://randomuser.me/api/portraits/men/35.jpg"
            ),
            KematianModel(
                "Sri Wahyuni",
                "147108510663001",
                "Perempuan",
                "14/02/2026",
                "Rumah Kediaman",
                "Diabetes",
                "SKM-92823",
                "Jl. Kartini No. 8B",
                "https://randomuser.me/api/portraits/women/36.jpg"
            ),
            KematianModel(
                "Joko Widodo",
                "147102150854002",
                "Laki-laki",
                "03/03/2026",
                "RS Awal Bros",
                "Gagal Ginjal",
                "SKM-92824",
                "Jl. Jenderal Sudirman No. 102",
                "https://randomuser.me/api/portraits/men/37.jpg"
            ),
            KematianModel(
                "Sumiati",
                "147109621151004",
                "Perempuan",
                "11/03/2026",
                "Rumah Kediaman",
                "Asma",
                "SKM-92825",
                "Jl. Riau Gang Guru",
                "https://randomuser.me/api/portraits/women/38.jpg"
            ),
            KematianModel(
                "Hendra Wijaya",
                "147104050481002",
                "Laki-laki",
                "25/03/2026",
                "Puskesmas Simpang Tiga",
                "Hipertensi",
                "SKM-92826",
                "Jl. Kaharuddin Nasution No. 5",
                "https://randomuser.me/api/portraits/men/39.jpg"
            ),
            KematianModel(
                "Anisa Fitri",
                "147107440995001",
                "Perempuan",
                "04/04/2026",
                "RS Sansani",
                "Demam Berdarah",
                "SKM-92827",
                "Jl. Soekarno Hatta Blok M",
                "https://randomuser.me/api/portraits/women/40.jpg"
            ),
            KematianModel(
                "Rudi Hartono",
                "147101230773003",
                "Laki-laki",
                "15/04/2026",
                "RSUD Arifin Achmad",
                "Kanker Paru",
                "SKM-92828",
                "Jl. Arifin Achmad No. 22",
                "https://randomuser.me/api/portraits/men/41.jpg"
            ),
            KematianModel(
                "Kartini",
                "147106520442001",
                "Perempuan",
                "29/04/2026",
                "Rumah Kediaman",
                "Sakit Tua",
                "SKM-92829",
                "Jl. Melati Indah Gg. Damai",
                "https://randomuser.me/api/portraits/women/42.jpg"
            ),
            KematianModel(
                "Dedi Setiawan",
                "147103190885002",
                "Laki-laki",
                "06/05/2026",
                "RS Ibnu Sina",
                "Asam Lambung Akut",
                "SKM-92830",
                "Jl. Ahmad Yani No. 45",
                "https://randomuser.me/api/portraits/men/43.jpg"
            ),
            KematianModel(
                "Mega Utami",
                "147105551289001",
                "Perempuan",
                "16/05/2026",
                "Rumah Kediaman",
                "Kanker Payudara",
                "SKM-92831",
                "Jl. Tanjung Datuk No. 14",
                "https://randomuser.me/api/portraits/women/44.jpg"
            ),
            KematianModel(
                "Mulyono",
                "147102110260003",
                "Laki-laki",
                "28/05/2026",
                "RSUD Arifin Achmad",
                "Penyakit Jantung",
                "SKM-92832",
                "Jl. Kulim Gg. Baiturrahman",
                "https://randomuser.me/api/portraits/men/45.jpg"
            ),
            KematianModel(
                "Evi Tamala",
                "147108420371002",
                "Perempuan",
                "05/06/2026",
                "RS PMC",
                "Stroke",
                "SKM-92833",
                "Jl. Lembaga Permasyarakatan",
                "https://randomuser.me/api/portraits/women/46.jpg"
            ),
            KematianModel(
                "Budi Santoso",
                "147101010166005",
                "Laki-laki",
                "12/06/2026",
                "Rumah Kediaman",
                "Sakit Tua",
                "SKM-92834",
                "Jl. Harapan Raya Gg. Sabar",
                "https://randomuser.me/api/portraits/men/47.jpg"
            ),
            KematianModel(
                "Yulianti",
                "147106440783001",
                "Perempuan",
                "22/06/2026",
                "RS Petala Bumi",
                "Infeksi Paru",
                "SKM-92835",
                "Jl. Dr. Sutomo No. 71",
                "https://randomuser.me/api/portraits/women/48.jpg"
            ),
            KematianModel(
                "Andi Permana",
                "147104121292002",
                "Laki-laki",
                "03/07/2026",
                "RS Aulia",
                "Kecelakaan Kerja",
                "SKM-92836",
                "Jl. Kubang Raya Km 2",
                "https://randomuser.me/api/portraits/men/49.jpg"
            ),
            KematianModel(
                "Siti Khadijah",
                "147105610550001",
                "Perempuan",
                "14/07/2026",
                "Rumah Kediaman",
                "Sakit Tua",
                "SKM-92837",
                "Jl. Sekolah No. 9 Rumbai",
                "https://randomuser.me/api/portraits/women/50.jpg"
            ),
            KematianModel(
                "Fajar Nugraha",
                "147103250688002",
                "Laki-laki",
                "25/07/2026",
                "RSUD Arifin Achmad",
                "Gagal Hati",
                "SKM-92838",
                "Jl. Paus Gg. Beranti",
                "https://randomuser.me/api/portraits/men/51.jpg"
            ),
            KematianModel(
                "Diana Lestari",
                "147109550893003",
                "Perempuan",
                "02/08/2026",
                "RS Eka Hospital",
                "Leukemia",
                "SKM-92839",
                "Jl. Soekarno Hatta No. 120",
                "https://randomuser.me/api/portraits/women/52.jpg"
            ),
            KematianModel(
                "Roni Hidayat",
                "147101080479001",
                "Laki-laki",
                "11/08/2026",
                "Rumah Kediaman",
                "Serangan Jantung",
                "SKM-92840",
                "Jl. Durian Gg. Rambutan",
                "https://randomuser.me/api/portraits/men/53.jpg"
            ),
            KematianModel(
                "Endang Sri",
                "147106691055002",
                "Perempuan",
                "20/08/2026",
                "RS Hermina",
                "Komplikasi Ginjal",
                "SKM-92841",
                "Jl. Tuanku Tambusai Ujung",
                "https://randomuser.me/api/portraits/women/54.jpg"
            ),
            KematianModel(
                "Agus Zalukhu",
                "147104190874004",
                "Laki-laki",
                "01/09/2026",
                "Rumah Kediaman",
                "TBC",
                "SKM-92842",
                "Jl. Sembilang No. 34",
                "https://randomuser.me/api/portraits/men/55.jpg"
            ),
            KematianModel(
                "Putri Ayu",
                "147107520101001",
                "Perempuan",
                "10/09/2026",
                "RSUD Arifin Achmad",
                "Demam Tinggi",
                "SKM-92843",
                "Jl. KH. Ahmad Dahlan No. 8",
                "https://randomuser.me/api/portraits/women/56.jpg"
            ),
            KematianModel(
                "Zulkifli",
                "147102040658002",
                "Laki-laki",
                "22/09/2026",
                "RS Tabrani",
                "Penyakit Paru Obstruktif",
                "SKM-92844",
                "Jl. Imam Munandar No. 88",
                "https://randomuser.me/api/portraits/men/57.jpg"
            ),
            KematianModel(
                "Rusmini",
                "147108441149001",
                "Perempuan",
                "04/10/2026",
                "Rumah Kediaman",
                "Sakit Tua",
                "SKM-92845",
                "Jl. Pepaya Gg. Nangka",
                "https://randomuser.me/api/portraits/women/58.jpg"
            ),
            KematianModel(
                "Yusuf Mansur",
                "147103290380002",
                "Laki-laki",
                "15/10/2026",
                "RSUD Arifin Achmad",
                "Penyakit Liver",
                "SKM-92846",
                "Jl. Dahlia No. 54",
                "https://randomuser.me/api/portraits/men/59.jpg"
            ),
            KematianModel(
                "Santi Rahayu",
                "147106701294003",
                "Perempuan",
                "27/10/2026",
                "Rumah Kediaman",
                "Kanker Rahim",
                "SKM-92847",
                "Jl. Cempaka No. 19",
                "https://randomuser.me/api/portraits/women/60.jpg"
            )
        )

        binding.rvKematian.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = KematianAdapter(listData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}