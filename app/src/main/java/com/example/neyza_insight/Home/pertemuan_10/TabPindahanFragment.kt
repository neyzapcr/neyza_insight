package com.example.neyza_insight.Home.pertemuan_10

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neyza_insight.R
import com.example.neyza_insight.data.AppDatabase
import com.example.neyza_insight.data.entity.PindahanEntity
import com.example.neyza_insight.databinding.FragmentTabPindahanBinding
import com.example.neyza_insight.reminder.ReminderDialogHelper
import kotlinx.coroutines.launch

class TabPindahanFragment : Fragment() {
    private var _binding: FragmentTabPindahanBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase
    private lateinit var adapter: PindahanAdapter
    private val listData = mutableListOf<PindahanEntity>()
    private val allDataList = mutableListOf<PindahanEntity>()
    private var selectedFilter = "Semua"

    private val formLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data
            val showSnackbar = data?.getBooleanExtra("SHOW_SNACKBAR", false) ?: false
            if (showSnackbar) {
                val draftId = data?.getIntExtra("DRAFT_ID", 0) ?: 0
                val eventType = data?.getStringExtra("EVENT_TYPE") ?: ""
                showDraftSavedSnackbar(eventType, draftId)
            }
        }
    }

    private fun showDraftSavedSnackbar(eventType: String, draftId: Int) {
        val snackbar = com.google.android.material.snackbar.Snackbar.make(
            binding.root,
            "Draft tersimpan",
            com.google.android.material.snackbar.Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Atur Reminder") {
            ReminderDialogHelper.showDraftReminderDialog(requireContext(), eventType, draftId)
        }
        snackbar.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTabPindahanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireContext())
        adapter = PindahanAdapter(
            listPindahan = listData,
            onDeleteClick = { entity ->
                deletePindahan(entity)
            },
            onItemClick = { entity ->
                if (entity.status == "Draft") {
                    val intent = Intent(requireContext(), PindahanFormActivity::class.java).apply {
                        putExtra("EXTRA_DRAFT_ID", entity.id)
                    }
                    formLauncher.launch(intent)
                }
            },
            onReminderClick = { entity ->
                ReminderDialogHelper.showReminderDialog(requireContext(), 2)
            }
        )

        binding.rvPindahan.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@TabPindahanFragment.adapter
        }

        // Set up ChipGroup filter listener
        binding.chipGroupFilter.setOnCheckedStateChangeListener { group, checkedIds ->
            val checkedId = checkedIds.firstOrNull()
            selectedFilter = when (checkedId) {
                R.id.chipDraft -> "Draft"
                R.id.chipSelesai -> "Selesai"
                else -> "Semua"
            }
            applyFilter()
        }

        // Set up FAB click listener
        binding.fabAddPindahan.setOnClickListener {
            val intent = Intent(requireContext(), PindahanFormActivity::class.java)
            formLauncher.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Check if opened from notification
        val activityIntent = requireActivity().intent
        val filterDraft = activityIntent.getBooleanExtra("FILTER_DRAFT", false)
        val targetTab = activityIntent.getIntExtra("TARGET_TAB", -1)
        if (filterDraft && targetTab == 2) {
            binding.chipGroupFilter.check(R.id.chipDraft)
            selectedFilter = "Draft"
            activityIntent.removeExtra("FILTER_DRAFT")
        }

        fetchAndSeedData()
    }

    private fun fetchAndSeedData() {
        lifecycleScope.launch {
            var data = db.pindahanDao().getAll()
            if (data.isEmpty()) {
                // Seed database with initial 30 mock items
                val initialList = getInitialSeedList()
                db.pindahanDao().insertAll(initialList)
                data = db.pindahanDao().getAll()
            }
            allDataList.clear()
            allDataList.addAll(data)
            applyFilter()
        }
    }

    private fun applyFilter() {
        listData.clear()
        when (selectedFilter) {
            "Draft" -> listData.addAll(allDataList.filter { it.status == "Draft" })
            "Selesai" -> listData.addAll(allDataList.filter { it.status == "Selesai" })
            else -> listData.addAll(allDataList)
        }
        adapter.notifyDataSetChanged()
    }

    private fun deletePindahan(entity: PindahanEntity) {
        lifecycleScope.launch {
            db.pindahanDao().delete(entity)
            fetchAndSeedData()
        }
    }

    private fun getInitialSeedList(): List<PindahanEntity> {
        return listOf(
            PindahanEntity(
                nama = "Rian Hidayat",
                nik = "3273019283019",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "12/04/2026",
                alamatAsal = "Bandung",
                alamatTujuan = "Pekanbaru Kota",
                alasanPindah = "Pekerjaan",
                noSuratPindah = "SP-88291",
                imageUrl = "https://randomuser.me/api/portraits/men/61.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Anisa Rahma",
                nik = "3273059201938",
                jenisKelamin = "Perempuan",
                tanggalPindah = "18/05/2026",
                alamatAsal = "Medan",
                alamatTujuan = "Siak",
                alasanPindah = "Ikut Keluarga",
                noSuratPindah = "SP-99201",
                imageUrl = "https://randomuser.me/api/portraits/women/62.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Bambang Sugeng",
                nik = "3273021928301",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "20/01/2026",
                alamatAsal = "Semarang",
                alamatTujuan = "Pekanbaru",
                alasanPindah = "Pekerjaan",
                noSuratPindah = "SP-88202",
                imageUrl = "https://randomuser.me/api/portraits/men/63.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Siti Aminah",
                nik = "1471054829102",
                jenisKelamin = "Perempuan",
                tanggalPindah = "22/01/2026",
                alamatAsal = "Kampar",
                alamatTujuan = "Jakarta Pusat",
                alasanPindah = "Pendidikan",
                noSuratPindah = "SP-88203",
                imageUrl = "https://randomuser.me/api/portraits/women/64.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Andika Pratama",
                nik = "3171031902831",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "05/02/2026",
                alamatAsal = "Jakarta Selatan",
                alamatTujuan = "Surabaya",
                alasanPindah = "Menikah",
                noSuratPindah = "SP-88204",
                imageUrl = "https://randomuser.me/api/portraits/men/65.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Dewi Lestari",
                nik = "1271045920193",
                jenisKelamin = "Perempuan",
                tanggalPindah = "14/02/2026",
                alamatAsal = "Medan",
                alamatTujuan = "Batam",
                alasanPindah = "Pekerjaan",
                noSuratPindah = "SP-88205",
                imageUrl = "https://randomuser.me/api/portraits/women/66.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Rendra Wijaya",
                nik = "3578021102938",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "03/03/2026",
                alamatAsal = "Surabaya",
                alamatTujuan = "Malang",
                alasanPindah = "Ikut Orang Tua",
                noSuratPindah = "SP-88206",
                imageUrl = "https://randomuser.me/api/portraits/men/67.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Mega Utami",
                nik = "6471034928103",
                jenisKelamin = "Perempuan",
                tanggalPindah = "10/03/2026",
                alamatAsal = "Balikpapan",
                alamatTujuan = "Yogyakarta",
                alasanPindah = "Pendidikan",
                noSuratPindah = "SP-88207",
                imageUrl = "https://randomuser.me/api/portraits/women/68.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Joko Susilo",
                nik = "3374012938102",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "19/03/2026",
                alamatAsal = "Semarang",
                alamatTujuan = "Solo",
                alasanPindah = "Pekerjaan",
                noSuratPindah = "SP-88208",
                imageUrl = "https://randomuser.me/api/portraits/men/69.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Fitri Handayani",
                nik = "1408025928103",
                jenisKelamin = "Perempuan",
                tanggalPindah = "25/03/2026",
                alamatAsal = "Siak",
                alamatTujuan = "Pekanbaru",
                alasanPindah = "Ikut Suami",
                noSuratPindah = "SP-88209",
                imageUrl = "https://randomuser.me/api/portraits/women/70.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Budi Utomo",
                nik = "3204121928304",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "02/04/2026",
                alamatAsal = "Garut",
                alamatTujuan = "Bandung Kota",
                alasanPindah = "Kesehatan",
                noSuratPindah = "SP-88210",
                imageUrl = "https://randomuser.me/api/portraits/men/71.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Ayu Saraswati",
                nik = "5171034920193",
                jenisKelamin = "Perempuan",
                tanggalPindah = "15/04/2026",
                alamatAsal = "Denpasar",
                alamatTujuan = "Jakarta Barat",
                alasanPindah = "Pekerjaan",
                noSuratPindah = "SP-88211",
                imageUrl = "https://randomuser.me/api/portraits/women/72.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Hendra Setiawan",
                nik = "2171021938291",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "28/04/2026",
                alamatAsal = "Batam",
                alamatTujuan = "Tanjungpinang",
                alasanPindah = "Urusan Keluarga",
                noSuratPindah = "SP-88212",
                imageUrl = "https://randomuser.me/api/portraits/men/73.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Larasati Putri",
                nik = "3471014930193",
                jenisKelamin = "Perempuan",
                tanggalPindah = "04/05/2026",
                alamatAsal = "Yogyakarta",
                alamatTujuan = "Sleman",
                alasanPindah = "Domisili Baru",
                noSuratPindah = "SP-88213",
                imageUrl = "https://randomuser.me/api/portraits/women/74.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Muhammad Rizky",
                nik = "7371052910392",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "12/05/2026",
                alamatAsal = "Makassar",
                alamatTujuan = "Kendari",
                alasanPindah = "Pekerjaan",
                noSuratPindah = "SP-88214",
                imageUrl = "https://randomuser.me/api/portraits/men/75.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Sri Wahyuni",
                nik = "3318035928103",
                jenisKelamin = "Perempuan",
                tanggalPindah = "20/05/2026",
                alamatAsal = "Pati",
                alamatTujuan = "Semarang",
                alasanPindah = "Ikut Anak",
                noSuratPindah = "SP-88215",
                imageUrl = "https://randomuser.me/api/portraits/women/76.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Eko Prasetyo",
                nik = "3515041928301",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "02/06/2026",
                alamatAsal = "Sidoarjo",
                alamatTujuan = "Surabaya",
                alasanPindah = "Pekerjaan",
                noSuratPindah = "SP-88216",
                imageUrl = "https://randomuser.me/api/portraits/men/77.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Citra Kirana",
                nik = "3273224920193",
                jenisKelamin = "Perempuan",
                tanggalPindah = "08/06/2026",
                alamatAsal = "Bandung",
                alamatTujuan = "Jakarta Selatan",
                alasanPindah = "Menikah",
                noSuratPindah = "SP-88217",
                imageUrl = "https://randomuser.me/api/portraits/women/78.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Agus Setiawan",
                nik = "1403011938291",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "15/06/2026",
                alamatAsal = "Bengkalis",
                alamatTujuan = "Dumai",
                alasanPindah = "Pekerjaan",
                noSuratPindah = "SP-88218",
                imageUrl = "https://randomuser.me/api/portraits/men/79.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Nabila Nuraini",
                nik = "3175045920193",
                jenisKelamin = "Perempuan",
                tanggalPindah = "22/06/2026",
                alamatAsal = "Jakarta Timur",
                alamatTujuan = "Bogor",
                alasanPindah = "Domisili Baru",
                noSuratPindah = "SP-88219",
                imageUrl = "https://randomuser.me/api/portraits/women/80.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Dedi Wijaya",
                nik = "1671031928301",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "01/07/2026",
                alamatAsal = "Palembang",
                alamatTujuan = "Lampung",
                alasanPindah = "Pekerjaan",
                noSuratPindah = "SP-88220",
                imageUrl = "https://randomuser.me/api/portraits/men/81.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Putri Amalia",
                nik = "1171024920193",
                jenisKelamin = "Perempuan",
                tanggalPindah = "07/07/2026",
                alamatAsal = "Banda Aceh",
                alamatTujuan = "Medan",
                alasanPindah = "Pendidikan",
                noSuratPindah = "SP-88221",
                imageUrl = "https://randomuser.me/api/portraits/women/82.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Roni Hidayat",
                nik = "6371021938291",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "14/07/2026",
                alamatAsal = "Banjarmasin",
                alamatTujuan = "Banjarbaru",
                alasanPindah = "Urusan Keluarga",
                noSuratPindah = "SP-88222",
                imageUrl = "https://randomuser.me/api/portraits/men/83.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Indah Permatasari",
                nik = "7171035928103",
                jenisKelamin = "Perempuan",
                tanggalPindah = "21/07/2026",
                alamatAsal = "Manado",
                alamatTujuan = "Gorontalo",
                alasanPindah = "Ikut Keluarga",
                noSuratPindah = "SP-88223",
                imageUrl = "https://randomuser.me/api/portraits/women/84.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Fajar Nugraha",
                nik = "3201041928301",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "03/08/2026",
                alamatAsal = "Bogor",
                alamatTujuan = "Sukabumi",
                alasanPindah = "Pekerjaan",
                noSuratPindah = "SP-88224",
                imageUrl = "https://randomuser.me/api/portraits/men/85.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Anisa Fitriani",
                nik = "1871024920193",
                jenisKelamin = "Perempuan",
                tanggalPindah = "11/08/2026",
                alamatAsal = "Lampung",
                alamatTujuan = "Jakarta Barat",
                alasanPindah = "Pendidikan",
                noSuratPindah = "SP-88225",
                imageUrl = "https://randomuser.me/api/portraits/women/86.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Taufik Rahman",
                nik = "1402011938291",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "19/08/2026",
                alamatAsal = "Indragiri Hulu",
                alamatTujuan = "Pekanbaru",
                alasanPindah = "Pekerjaan",
                noSuratPindah = "SP-88226",
                imageUrl = "https://randomuser.me/api/portraits/men/87.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Dina Mariana",
                nik = "3172035928103",
                jenisKelamin = "Perempuan",
                tanggalPindah = "26/08/2026",
                alamatAsal = "Jakarta Utara",
                alamatTujuan = "Tangerang",
                alasanPindah = "Domisili Baru",
                noSuratPindah = "SP-88227",
                imageUrl = "https://randomuser.me/api/portraits/women/88.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Yusuf Mansur",
                nik = "3671021938291",
                jenisKelamin = "Laki-laki",
                tanggalPindah = "02/09/2026",
                alamatAsal = "Tangerang",
                alamatTujuan = "Serang",
                alasanPindah = "Urusan Keluarga",
                noSuratPindah = "SP-88228",
                imageUrl = "https://randomuser.me/api/portraits/men/89.jpg",
                status = "Selesai"
            ),
            PindahanEntity(
                nama = "Santi Rahayu",
                nik = "3205034920193",
                jenisKelamin = "Perempuan",
                tanggalPindah = "09/09/2026",
                alamatAsal = "Garut",
                alamatTujuan = "Tasikmalaya",
                alasanPindah = "Ikut Suami",
                noSuratPindah = "SP-88229",
                imageUrl = "https://randomuser.me/api/portraits/women/90.jpg",
                status = "Selesai"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}