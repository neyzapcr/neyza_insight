package com.example.neyza_insight.Home.pertemuan_10

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neyza_insight.R
import com.example.neyza_insight.databinding.FragmentTabPindahanBinding

class TabPindahanFragment : Fragment() {
    private var _binding: FragmentTabPindahanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTabPindahanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listData = listOf(
            PindahanModel(
                "Rian Hidayat",
                "3273019283019",
                "Laki-laki",
                "12/04/2026",
                "Bandung",
                "Pekanbaru Kota",
                "Pekerjaan",
                "SP-88291",
                "https://randomuser.me/api/portraits/men/61.jpg"
            ),
            PindahanModel(
                "Anisa Rahma",
                "3273059201938",
                "Perempuan",
                "18/05/2026",
                "Medan",
                "Siak",
                "Ikut Keluarga",
                "SP-99201",
                "https://randomuser.me/api/portraits/women/62.jpg"
            ),
            PindahanModel(
                "Bambang Sugeng",
                "3273021928301",
                "Laki-laki",
                "20/01/2026",
                "Semarang",
                "Pekanbaru",
                "Pekerjaan",
                "SP-88202",
                "https://randomuser.me/api/portraits/men/63.jpg"
            ),
            PindahanModel(
                "Siti Aminah",
                "1471054829102",
                "Perempuan",
                "22/01/2026",
                "Kampar",
                "Jakarta Pusat",
                "Pendidikan",
                "SP-88203",
                "https://randomuser.me/api/portraits/women/64.jpg"
            ),
            PindahanModel(
                "Andika Pratama",
                "3171031902831",
                "Laki-laki",
                "05/02/2026",
                "Jakarta Selatan",
                "Surabaya",
                "Menikah",
                "SP-88204",
                "https://randomuser.me/api/portraits/men/65.jpg"
            ),
            PindahanModel(
                "Dewi Lestari",
                "1271045920193",
                "Perempuan",
                "14/02/2026",
                "Medan",
                "Batam",
                "Pekerjaan",
                "SP-88205",
                "https://randomuser.me/api/portraits/women/66.jpg"
            ),
            PindahanModel(
                "Rendra Wijaya",
                "3578021102938",
                "Laki-laki",
                "03/03/2026",
                "Surabaya",
                "Malang",
                "Ikut Orang Tua",
                "SP-88206",
                "https://randomuser.me/api/portraits/men/67.jpg"
            ),
            PindahanModel(
                "Mega Utami",
                "6471034928103",
                "Perempuan",
                "10/03/2026",
                "Balikpapan",
                "Yogyakarta",
                "Pendidikan",
                "SP-88207",
                "https://randomuser.me/api/portraits/women/68.jpg"
            ),
            PindahanModel(
                "Joko Susilo",
                "3374012938102",
                "Laki-laki",
                "19/03/2026",
                "Semarang",
                "Solo",
                "Pekerjaan",
                "SP-88208",
                "https://randomuser.me/api/portraits/men/69.jpg"
            ),
            PindahanModel(
                "Fitri Handayani",
                "1408025928103",
                "Perempuan",
                "25/03/2026",
                "Siak",
                "Pekanbaru",
                "Ikut Suami",
                "SP-88209",
                "https://randomuser.me/api/portraits/women/70.jpg"
            ),
            PindahanModel(
                "Budi Utomo",
                "3204121928304",
                "Laki-laki",
                "02/04/2026",
                "Garut",
                "Bandung Kota",
                "Kesehatan",
                "SP-88210",
                "https://randomuser.me/api/portraits/men/71.jpg"
            ),
            PindahanModel(
                "Ayu Saraswati",
                "5171034920193",
                "Perempuan",
                "15/04/2026",
                "Denpasar",
                "Jakarta Barat",
                "Pekerjaan",
                "SP-88211",
                "https://randomuser.me/api/portraits/women/72.jpg"
            ),
            PindahanModel(
                "Hendra Setiawan",
                "2171021938291",
                "Laki-laki",
                "28/04/2026",
                "Batam",
                "Tanjungpinang",
                "Urusan Keluarga",
                "SP-88212",
                "https://randomuser.me/api/portraits/men/73.jpg"
            ),
            PindahanModel(
                "Larasati Putri",
                "3471014930193",
                "Perempuan",
                "04/05/2026",
                "Yogyakarta",
                "Sleman",
                "Domisili Baru",
                "SP-88213",
                "https://randomuser.me/api/portraits/women/74.jpg"
            ),
            PindahanModel(
                "Muhammad Rizky",
                "7371052910392",
                "Laki-laki",
                "12/05/2026",
                "Makassar",
                "Kendari",
                "Pekerjaan",
                "SP-88214",
                "https://randomuser.me/api/portraits/men/75.jpg"
            ),
            PindahanModel(
                "Sri Wahyuni",
                "3318035928103",
                "Perempuan",
                "20/05/2026",
                "Pati",
                "Semarang",
                "Ikut Anak",
                "SP-88215",
                "https://randomuser.me/api/portraits/women/76.jpg"
            ),
            PindahanModel(
                "Eko Prasetyo",
                "3515041928301",
                "Laki-laki",
                "02/06/2026",
                "Sidoarjo",
                "Surabaya",
                "Pekerjaan",
                "SP-88216",
                "https://randomuser.me/api/portraits/men/77.jpg"
            ),
            PindahanModel(
                "Citra Kirana",
                "3273224920193",
                "Perempuan",
                "08/06/2026",
                "Bandung",
                "Jakarta Selatan",
                "Menikah",
                "SP-88217",
                "https://randomuser.me/api/portraits/women/78.jpg"
            ),
            PindahanModel(
                "Agus Setiawan",
                "1403011938291",
                "Laki-laki",
                "15/06/2026",
                "Bengkalis",
                "Dumai",
                "Pekerjaan",
                "SP-88218",
                "https://randomuser.me/api/portraits/men/79.jpg"
            ),
            PindahanModel(
                "Nabila Nuraini",
                "3175045920193",
                "Perempuan",
                "22/06/2026",
                "Jakarta Timur",
                "Bogor",
                "Domisili Baru",
                "SP-88219",
                "https://randomuser.me/api/portraits/women/80.jpg"
            ),
            PindahanModel(
                "Dedi Wijaya",
                "1671031928301",
                "Laki-laki",
                "01/07/2026",
                "Palembang",
                "Lampung",
                "Pekerjaan",
                "SP-88220",
                "https://randomuser.me/api/portraits/men/81.jpg"
            ),
            PindahanModel(
                "Putri Amalia",
                "1171024920193",
                "Perempuan",
                "07/07/2026",
                "Banda Aceh",
                "Medan",
                "Pendidikan",
                "SP-88221",
                "https://randomuser.me/api/portraits/women/82.jpg"
            ),
            PindahanModel(
                "Roni Hidayat",
                "6371021938291",
                "Laki-laki",
                "14/07/2026",
                "Banjarmasin",
                "Banjarbaru",
                "Urusan Keluarga",
                "SP-88222",
                "https://randomuser.me/api/portraits/men/83.jpg"
            ),
            PindahanModel(
                "Indah Permatasari",
                "7171035928103",
                "Perempuan",
                "21/07/2026",
                "Manado",
                "Gorontalo",
                "Ikut Keluarga",
                "SP-88223",
                "https://randomuser.me/api/portraits/women/84.jpg"
            ),
            PindahanModel(
                "Fajar Nugraha",
                "3201041928301",
                "Laki-laki",
                "03/08/2026",
                "Bogor",
                "Sukabumi",
                "Pekerjaan",
                "SP-88224",
                "https://randomuser.me/api/portraits/men/85.jpg"
            ),
            PindahanModel(
                "Anisa Fitriani",
                "1871024920193",
                "Perempuan",
                "11/08/2026",
                "Lampung",
                "Jakarta Barat",
                "Pendidikan",
                "SP-88225",
                "https://randomuser.me/api/portraits/women/86.jpg"
            ),
            PindahanModel(
                "Taufik Rahman",
                "1402011938291",
                "Laki-laki",
                "19/08/2026",
                "Indragiri Hulu",
                "Pekanbaru",
                "Pekerjaan",
                "SP-88226",
                "https://randomuser.me/api/portraits/men/87.jpg"
            ),
            PindahanModel(
                "Dina Mariana",
                "3172035928103",
                "Perempuan",
                "26/08/2026",
                "Jakarta Utara",
                "Tangerang",
                "Domisili Baru",
                "SP-88227",
                "https://randomuser.me/api/portraits/women/88.jpg"
            ),
            PindahanModel(
                "Yusuf Mansur",
                "3671021938291",
                "Laki-laki",
                "02/09/2026",
                "Tangerang",
                "Serang",
                "Urusan Keluarga",
                "SP-88228",
                "https://randomuser.me/api/portraits/men/89.jpg"
            ),
            PindahanModel(
                "Santi Rahayu",
                "3205034920193",
                "Perempuan",
                "09/09/2026",
                "Garut",
                "Tasikmalaya",
                "Ikut Suami",
                "SP-88229",
                "https://randomuser.me/api/portraits/women/90.jpg"
            )
        )

        binding.rvPindahan.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = PindahanAdapter(listData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}