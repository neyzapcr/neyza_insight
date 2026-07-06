package com.example.neyza_insight.Home.pertemuan_10

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neyza_insight.data.AppDatabase
import com.example.neyza_insight.data.entity.KelahiranEntity
import com.example.neyza_insight.databinding.FragmentTabKelahiranBinding
import com.example.neyza_insight.R
import com.example.neyza_insight.reminder.ReminderDialogHelper
import kotlinx.coroutines.launch

class TabKelahiranFragment : Fragment() {
    private var _binding: FragmentTabKelahiranBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase
    private lateinit var adapter: KelahiranAdapter
    private val listData = mutableListOf<KelahiranEntity>()
    private val allDataList = mutableListOf<KelahiranEntity>()
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
        _binding = FragmentTabKelahiranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireContext())
        adapter = KelahiranAdapter(
            listKelahiran = listData,
            onDeleteClick = { entity ->
                deleteKelahiran(entity)
            },
            onItemClick = { entity ->
                if (entity.status == "Draft") {
                    val intent = Intent(requireContext(), KelahiranFormActivity::class.java).apply {
                        putExtra("EXTRA_DRAFT_ID", entity.id)
                    }
                    formLauncher.launch(intent)
                }
            },
            onReminderClick = { entity ->
                ReminderDialogHelper.showReminderDialog(requireContext(), 0)
            }
        )

        binding.rvKelahiran.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@TabKelahiranFragment.adapter
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
        binding.fabAddKelahiran.setOnClickListener {
            val intent = Intent(requireContext(), KelahiranFormActivity::class.java)
            formLauncher.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Check if opened from notification
        val activityIntent = requireActivity().intent
        val filterDraft = activityIntent.getBooleanExtra("FILTER_DRAFT", false)
        val targetTab = activityIntent.getIntExtra("TARGET_TAB", -1)
        if (filterDraft && targetTab == 0) {
            binding.chipGroupFilter.check(R.id.chipDraft)
            selectedFilter = "Draft"
            activityIntent.removeExtra("FILTER_DRAFT")
        }

        fetchAndSeedData()
    }

    private fun fetchAndSeedData() {
        lifecycleScope.launch {
            var data = db.kelahiranDao().getAll()
            if (data.isEmpty()) {
                // Seed database with initial 30 mock items
                val initialList = getInitialSeedList()
                db.kelahiranDao().insertAll(initialList)
                data = db.kelahiranDao().getAll()
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

    private fun deleteKelahiran(entity: KelahiranEntity) {
        lifecycleScope.launch {
            db.kelahiranDao().delete(entity)
            fetchAndSeedData()
        }
    }

    private fun getInitialSeedList(): List<KelahiranEntity> {
        return listOf(
            KelahiranEntity(
                nama = "Anggabaya Unggul Haryanto S.E.",
                noAkta = "AKTA-832819",
                tanggalLahir = "04/12/2026",
                tempatLahir = "Jakarta Barat",
                namaAyah = "Raihan Nababan",
                namaIbu = "Nabila Nuraini",
                imageUrl = "https://randomuser.me/api/portraits/men/1.jpg"
            ),
            KelahiranEntity(
                nama = "Siti Aminah",
                noAkta = "AKTA-110293",
                tanggalLahir = "15/01/2026",
                tempatLahir = "Pekanbaru",
                namaAyah = "Budi Santoso",
                namaIbu = "Siti Khadijah",
                imageUrl = "https://randomuser.me/api/portraits/women/2.jpg"
            ),
            KelahiranEntity(
                nama = "Aris Setiawan",
                noAkta = "AKTA-110294",
                tanggalLahir = "22/01/2026",
                tempatLahir = "Surabaya",
                namaAyah = "Joko Susilo",
                namaIbu = "Rini Ambarwati",
                imageUrl = "https://randomuser.me/api/portraits/men/3.jpg"
            ),
            KelahiranEntity(
                nama = "Dewi Lestari",
                noAkta = "AKTA-110295",
                tanggalLahir = "05/02/2026",
                tempatLahir = "Bandung",
                namaAyah = "Asep Sunandar",
                namaIbu = "Euis Dahlia",
                imageUrl = "https://randomuser.me/api/portraits/women/4.jpg"
            ),
            KelahiranEntity(
                nama = "Rian Hidayat",
                noAkta = "AKTA-110296",
                tanggalLahir = "14/02/2026",
                tempatLahir = "Medan",
                namaAyah = "Sitorus Pane",
                namaIbu = "Murniati",
                imageUrl = "https://randomuser.me/api/portraits/men/5.jpg"
            ),
            KelahiranEntity(
                nama = "Slamet Rahardjo",
                noAkta = "AKTA-110297",
                tanggalLahir = "03/03/2026",
                tempatLahir = "Yogyakarta",
                namaAyah = "Bambang Tri",
                namaIbu = "Sri Wahyuni",
                imageUrl = "https://randomuser.me/api/portraits/men/6.jpg"
            ),
            KelahiranEntity(
                nama = "Putu Ayu Saraswati",
                noAkta = "AKTA-110298",
                tanggalLahir = "19/03/2026",
                tempatLahir = "Denpasar",
                namaAyah = "I Wayan Sudarta",
                namaIbu = "Ni Luh Ketut",
                imageUrl = "https://randomuser.me/api/portraits/women/7.jpg"
            ),
            KelahiranEntity(
                nama = "Muhammad Rizky",
                noAkta = "AKTA-110299",
                tanggalLahir = "28/03/2026",
                tempatLahir = "Makassar",
                namaAyah = "Andi Ahmad",
                namaIbu = "Siti Fatimah",
                imageUrl = "https://randomuser.me/api/portraits/men/8.jpg"
            ),
            KelahiranEntity(
                nama = "Aditya Pratama",
                noAkta = "AKTA-110300",
                tanggalLahir = "09/04/2026",
                tempatLahir = "Semarang",
                namaAyah = "Dedi Wijaya",
                namaIbu = "Retno Palupi",
                imageUrl = "https://randomuser.me/api/portraits/men/9.jpg"
            ),
            KelahiranEntity(
                nama = "Fitri Handayani",
                noAkta = "AKTA-110301",
                tanggalLahir = "21/04/2026",
                tempatLahir = "Palembang",
                namaAyah = "Hasan Basri",
                namaIbu = "Rusmini",
                imageUrl = "https://randomuser.me/api/portraits/women/10.jpg"
            ),
            KelahiranEntity(
                nama = "Budi Utomo",
                noAkta = "AKTA-110302",
                tanggalLahir = "02/05/2026",
                tempatLahir = "Malang",
                namaAyah = "Agus Setiyono",
                namaIbu = "Endang Sri",
                imageUrl = "https://randomuser.me/api/portraits/men/11.jpg"
            ),
            KelahiranEntity(
                nama = "Nanda Saputra",
                noAkta = "AKTA-110303",
                tanggalLahir = "11/05/2026",
                tempatLahir = "Banjarmasin",
                namaAyah = "Hermanudin",
                namaIbu = "Siti Rahmah",
                imageUrl = "https://randomuser.me/api/portraits/men/12.jpg"
            ),
            KelahiranEntity(
                nama = "Citra Kirana",
                noAkta = "AKTA-110304",
                tanggalLahir = "25/05/2026",
                tempatLahir = "Bogor",
                namaAyah = "Cecep Rahman",
                namaIbu = "Neng Hasanah",
                imageUrl = "https://randomuser.me/api/portraits/women/13.jpg"
            ),
            KelahiranEntity(
                nama = "Dimas Anggara",
                noAkta = "AKTA-110305",
                tanggalLahir = "04/06/2026",
                tempatLahir = "Solo",
                namaAyah = "Triyono",
                namaIbu = "Siti Lestari",
                imageUrl = "https://randomuser.me/api/portraits/men/14.jpg"
            ),
            KelahiranEntity(
                nama = "Aulia Rahma",
                noAkta = "AKTA-110306",
                tanggalLahir = "18/06/2026",
                tempatLahir = "Padang",
                namaAyah = "Syamsul Bahri",
                namaIbu = "Ernawati",
                imageUrl = "https://randomuser.me/api/portraits/women/15.jpg"
            ),
            KelahiranEntity(
                nama = "Eko Prasetyo",
                noAkta = "AKTA-110307",
                tanggalLahir = "01/07/2026",
                tempatLahir = "Balikpapan",
                namaAyah = "Rudi Hartono",
                namaIbu = "Yulianti",
                imageUrl = "https://randomuser.me/api/portraits/men/16.jpg"
            ),
            KelahiranEntity(
                nama = "Indah Permatasari",
                noAkta = "AKTA-110308",
                tanggalLahir = "12/07/2026",
                tempatLahir = "Manado",
                namaAyah = "Frans Wowor",
                namaIbu = "Maria Lengkey",
                imageUrl = "https://randomuser.me/api/portraits/women/17.jpg"
            ),
            KelahiranEntity(
                nama = "Gilang Dirga",
                noAkta = "AKTA-110309",
                tanggalLahir = "29/07/2026",
                tempatLahir = "Jakarta Selatan",
                namaAyah = "Wendy",
                namaIbu = "Siska",
                imageUrl = "https://randomuser.me/api/portraits/men/18.jpg"
            ),
            KelahiranEntity(
                nama = "Lesti Kejora",
                noAkta = "AKTA-110310",
                tanggalLahir = "08/08/2026",
                tempatLahir = "Cianjur",
                namaAyah = "Endang Mulyana",
                namaIbu = "Sukmawati",
                imageUrl = "https://randomuser.me/api/portraits/women/19.jpg"
            ),
            KelahiranEntity(
                nama = "Rizky Billar",
                noAkta = "AKTA-110311",
                tanggalLahir = "17/08/2026",
                tempatLahir = "Medan",
                namaAyah = "Daniel",
                namaIbu = "Rosmala",
                imageUrl = "https://randomuser.me/api/portraits/men/20.jpg"
            ),
            KelahiranEntity(
                nama = "Hendra Setiawan",
                noAkta = "AKTA-110312",
                tanggalLahir = "03/09/2026",
                tempatLahir = "Jambi",
                namaAyah = "Agustinus",
                namaIbu = "Theresia",
                imageUrl = "https://randomuser.me/api/portraits/men/21.jpg"
            ),
            KelahiranEntity(
                nama = "Mega Utami",
                noAkta = "AKTA-110313",
                tanggalLahir = "15/09/2026",
                tempatLahir = "Pontianak",
                namaAyah = "Tan Wijaya",
                namaIbu = "Meilani",
                imageUrl = "https://randomuser.me/api/portraits/women/22.jpg"
            ),
            KelahiranEntity(
                nama = "Taufik Hidayat",
                noAkta = "AKTA-110314",
                tanggalLahir = "27/09/2026",
                tempatLahir = "Bandung",
                namaAyah = "Ahmad Haris",
                namaIbu = "Enung",
                imageUrl = "https://randomuser.me/api/portraits/men/23.jpg"
            ),
            KelahiranEntity(
                nama = "Yuni Shara",
                noAkta = "AKTA-110315",
                tanggalLahir = "10/10/2026",
                tempatLahir = "Batu",
                namaAyah = "Trenggono",
                namaIbu = "Rachma Widadiningsih",
                imageUrl = "https://randomuser.me/api/portraits/women/24.jpg"
            ),
            KelahiranEntity(
                nama = "Krisdayanti",
                noAkta = "AKTA-110316",
                tanggalLahir = "24/10/2026",
                tempatLahir = "Batu",
                namaAyah = "Trenggono",
                namaIbu = "Rachma Widadiningsih",
                imageUrl = "https://randomuser.me/api/portraits/women/25.jpg"
            ),
            KelahiranEntity(
                nama = "Anang Hermansyah",
                noAkta = "AKTA-110317",
                tanggalLahir = "05/11/2026",
                tempatLahir = "Jember",
                namaAyah = "Cholib",
                namaIbu = "Anissa",
                imageUrl = "https://randomuser.me/api/portraits/men/26.jpg"
            ),
            KelahiranEntity(
                nama = "Ashanty Siddik",
                noAkta = "AKTA-110318",
                tanggalLahir = "16/11/2026",
                tempatLahir = "Jakarta",
                namaAyah = "Soejahjo Hasnoputro",
                namaIbu = "Farida",
                imageUrl = "https://randomuser.me/api/portraits/women/27.jpg"
            ),
            KelahiranEntity(
                nama = "Aurel Hermansyah",
                noAkta = "AKTA-110319",
                tanggalLahir = "30/11/2026",
                tempatLahir = "Jakarta Pusat",
                namaAyah = "Anang Hermansyah",
                namaIbu = "Krisdayanti",
                imageUrl = "https://randomuser.me/api/portraits/women/28.jpg"
            ),
            KelahiranEntity(
                nama = "Atta Halilintar",
                noAkta = "AKTA-110320",
                tanggalLahir = "11/12/2026",
                tempatLahir = "Dumai",
                namaAyah = "Halilintar Anofial",
                namaIbu = "Lenggogeni Faruk",
                imageUrl = "https://randomuser.me/api/portraits/men/29.jpg"
            ),
            KelahiranEntity(
                nama = "Thariq Halilintar",
                noAkta = "AKTA-110321",
                tanggalLahir = "25/12/2026",
                tempatLahir = "Brunei",
                namaAyah = "Halilintar Anofial",
                namaIbu = "Lenggogeni Faruk",
                imageUrl = "https://randomuser.me/api/portraits/men/30.jpg"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}