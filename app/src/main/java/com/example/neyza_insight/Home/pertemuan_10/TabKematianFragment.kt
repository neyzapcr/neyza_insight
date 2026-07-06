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
import com.example.neyza_insight.data.entity.KematianEntity
import com.example.neyza_insight.databinding.FragmentTabKematianBinding
import com.example.neyza_insight.R
import com.example.neyza_insight.reminder.ReminderDialogHelper
import kotlinx.coroutines.launch

class TabKematianFragment : Fragment() {
    private var _binding: FragmentTabKematianBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: AppDatabase
    private lateinit var adapter: KematianAdapter
    private val listData = mutableListOf<KematianEntity>()
    private val allDataList = mutableListOf<KematianEntity>()
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
        _binding = FragmentTabKematianBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = AppDatabase.getInstance(requireContext())
        adapter = KematianAdapter(
            listKematian = listData,
            onDeleteClick = { entity ->
                deleteKematian(entity)
            },
            onItemClick = { entity ->
                if (entity.status == "Draft") {
                    val intent = Intent(requireContext(), KematianFormActivity::class.java).apply {
                        putExtra("EXTRA_DRAFT_ID", entity.id)
                    }
                    formLauncher.launch(intent)
                } else {
                    showDetailDialog(entity)
                }
            },
            onReminderClick = { entity ->
                ReminderDialogHelper.showReminderDialog(requireContext(), 1)
            }
        )

        binding.rvKematian.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@TabKematianFragment.adapter
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
        binding.fabAddKematian.setOnClickListener {
            val intent = Intent(requireContext(), KematianFormActivity::class.java)
            formLauncher.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Check if opened from notification
        val activityIntent = requireActivity().intent
        val filterDraft = activityIntent.getBooleanExtra("FILTER_DRAFT", false)
        val targetTab = activityIntent.getIntExtra("TARGET_TAB", -1)
        if (filterDraft && targetTab == 1) {
            binding.chipGroupFilter.check(R.id.chipDraft)
            selectedFilter = "Draft"
            activityIntent.removeExtra("FILTER_DRAFT")
        }

        fetchAndSeedData()
    }

    private fun fetchAndSeedData() {
        lifecycleScope.launch {
            var data = db.kematianDao().getAll()
            if (data.isEmpty()) {
                // Seed database with initial 30 mock items
                val initialList = getInitialSeedList()
                db.kematianDao().insertAll(initialList)
                data = db.kematianDao().getAll()
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

    private fun deleteKematian(entity: KematianEntity) {
        lifecycleScope.launch {
            db.kematianDao().delete(entity)
            fetchAndSeedData()
        }
    }

    private fun getInitialSeedList(): List<KematianEntity> {
        return listOf(
            KematianEntity(
                nama = "Gibran Erlangga",
                nik = "1471020039480",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "20/03/2026",
                lokasi = "RSUD Arifin Achmad",
                sebabKematian = "Sakit Tua",
                noSurat = "SKM-92819",
                alamat = "Jl. Sudirman No. 45",
                imageUrl = "https://randomuser.me/api/portraits/men/31.jpg"
            ),
            KematianEntity(
                nama = "Dewi Lestari",
                nik = "1471092830193",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "02/05/2026",
                lokasi = "Rumah Kediaman",
                sebabKematian = "Serangan Jantung",
                noSurat = "SKM-10293",
                alamat = "Jl. Panam Blok C",
                imageUrl = "https://randomuser.me/api/portraits/women/32.jpg"
            ),
            KematianEntity(
                nama = "Bambang Sulistyo",
                nik = "147101020348001",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "12/01/2026",
                lokasi = "RSUD Arifin Achmad",
                sebabKematian = "Stroke",
                noSurat = "SKM-92820",
                alamat = "Jl. Tuanku Tambusai No. 12",
                imageUrl = "https://randomuser.me/api/portraits/men/33.jpg"
            ),
            KematianEntity(
                nama = "Siti Rahmah",
                nik = "147105421093002",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "18/01/2026",
                lokasi = "Rumah Kediaman",
                sebabKematian = "Sakit Tua",
                noSurat = "SKM-92821",
                alamat = "Jl. Soebrantas Samping Ponpes",
                imageUrl = "https://randomuser.me/api/portraits/women/34.jpg"
            ),
            KematianEntity(
                nama = "Ahmad Hidayat",
                nik = "147103120572003",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "02/02/2026",
                lokasi = "RS Jiwa Tampan",
                sebabKematian = "Komplikasi",
                noSurat = "SKM-92822",
                alamat = "Jl. Suka Karya Gg. Al-Ikhlas",
                imageUrl = "https://randomuser.me/api/portraits/men/35.jpg"
            ),
            KematianEntity(
                nama = "Sri Wahyuni",
                nik = "147108510663001",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "14/02/2026",
                lokasi = "Rumah Kediaman",
                sebabKematian = "Diabetes",
                noSurat = "SKM-92823",
                alamat = "Jl. Kartini No. 8B",
                imageUrl = "https://randomuser.me/api/portraits/women/36.jpg"
            ),
            KematianEntity(
                nama = "Joko Widodo",
                nik = "147102150854002",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "03/03/2026",
                lokasi = "RS Awal Bros",
                sebabKematian = "Gagal Ginjal",
                noSurat = "SKM-92824",
                alamat = "Jl. Jenderal Sudirman No. 102",
                imageUrl = "https://randomuser.me/api/portraits/men/37.jpg"
            ),
            KematianEntity(
                nama = "Sumiati",
                nik = "147109621151004",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "11/03/2026",
                lokasi = "Rumah Kediaman",
                sebabKematian = "Asma",
                noSurat = "SKM-92825",
                alamat = "Jl. Riau Gang Guru",
                imageUrl = "https://randomuser.me/api/portraits/women/38.jpg"
            ),
            KematianEntity(
                nama = "Hendra Wijaya",
                nik = "147104050481002",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "25/03/2026",
                lokasi = "Puskesmas Simpang Tiga",
                sebabKematian = "Hipertensi",
                noSurat = "SKM-92826",
                alamat = "Jl. Kaharuddin Nasution No. 5",
                imageUrl = "https://randomuser.me/api/portraits/men/39.jpg"
            ),
            KematianEntity(
                nama = "Anisa Fitri",
                nik = "147107440995001",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "04/04/2026",
                lokasi = "RS Sansani",
                sebabKematian = "Demam Berdarah",
                noSurat = "SKM-92827",
                alamat = "Jl. Soekarno Hatta Blok M",
                imageUrl = "https://randomuser.me/api/portraits/women/40.jpg"
            ),
            KematianEntity(
                nama = "Rudi Hartono",
                nik = "147101230773003",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "15/04/2026",
                lokasi = "RSUD Arifin Achmad",
                sebabKematian = "Kanker Paru",
                noSurat = "SKM-92828",
                alamat = "Jl. Arifin Achmad No. 22",
                imageUrl = "https://randomuser.me/api/portraits/men/41.jpg"
            ),
            KematianEntity(
                nama = "Kartini",
                nik = "147106520442001",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "29/04/2026",
                lokasi = "Rumah Kediaman",
                sebabKematian = "Sakit Tua",
                noSurat = "SKM-92829",
                alamat = "Jl. Melati Indah Gg. Damai",
                imageUrl = "https://randomuser.me/api/portraits/women/42.jpg"
            ),
            KematianEntity(
                nama = "Dedi Setiawan",
                nik = "147103190885002",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "06/05/2026",
                lokasi = "RS Ibnu Sina",
                sebabKematian = "Asam Lambung Akut",
                noSurat = "SKM-92830",
                alamat = "Jl. Ahmad Yani No. 45",
                imageUrl = "https://randomuser.me/api/portraits/men/43.jpg"
            ),
            KematianEntity(
                nama = "Mega Utami",
                nik = "147105551289001",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "16/05/2026",
                lokasi = "Rumah Kediaman",
                sebabKematian = "Kanker Payudara",
                noSurat = "SKM-92831",
                alamat = "Jl. Tanjung Datuk No. 14",
                imageUrl = "https://randomuser.me/api/portraits/women/44.jpg"
            ),
            KematianEntity(
                nama = "Mulyono",
                nik = "147102110260003",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "28/05/2026",
                lokasi = "RSUD Arifin Achmad",
                sebabKematian = "Penyakit Jantung",
                noSurat = "SKM-92832",
                alamat = "Jl. Kulim Gg. Baiturrahman",
                imageUrl = "https://randomuser.me/api/portraits/men/45.jpg"
            ),
            KematianEntity(
                nama = "Evi Tamala",
                nik = "147108420371002",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "05/06/2026",
                lokasi = "RS PMC",
                sebabKematian = "Stroke",
                noSurat = "SKM-92833",
                alamat = "Jl. Lembaga Permasyarakatan",
                imageUrl = "https://randomuser.me/api/portraits/women/46.jpg"
            ),
            KematianEntity(
                nama = "Budi Santoso",
                nik = "147101010166005",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "12/06/2026",
                lokasi = "Rumah Kediaman",
                sebabKematian = "Sakit Tua",
                noSurat = "SKM-92834",
                alamat = "Jl. Harapan Raya Gg. Sabar",
                imageUrl = "https://randomuser.me/api/portraits/men/47.jpg"
            ),
            KematianEntity(
                nama = "Yulianti",
                nik = "147106440783001",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "22/06/2026",
                lokasi = "RS Petala Bumi",
                sebabKematian = "Infeksi Paru",
                noSurat = "SKM-92835",
                alamat = "Jl. Dr. Sutomo No. 71",
                imageUrl = "https://randomuser.me/api/portraits/women/48.jpg"
            ),
            KematianEntity(
                nama = "Andi Permana",
                nik = "147104121292002",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "03/07/2026",
                lokasi = "RS Aulia",
                sebabKematian = "Kecelakaan Kerja",
                noSurat = "SKM-92836",
                alamat = "Jl. Kubang Raya Km 2",
                imageUrl = "https://randomuser.me/api/portraits/men/49.jpg"
            ),
            KematianEntity(
                nama = "Siti Khadijah",
                nik = "147105610550001",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "14/07/2026",
                lokasi = "Rumah Kediaman",
                sebabKematian = "Sakit Tua",
                noSurat = "SKM-92837",
                alamat = "Jl. Sekolah No. 9 Rumbai",
                imageUrl = "https://randomuser.me/api/portraits/women/50.jpg"
            ),
            KematianEntity(
                nama = "Fajar Nugraha",
                nik = "147103250688002",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "25/07/2026",
                lokasi = "RSUD Arifin Achmad",
                sebabKematian = "Gagal Hati",
                noSurat = "SKM-92838",
                alamat = "Jl. Paus Gg. Beranti",
                imageUrl = "https://randomuser.me/api/portraits/men/51.jpg"
            ),
            KematianEntity(
                nama = "Diana Lestari",
                nik = "147109550893003",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "02/08/2026",
                lokasi = "RS Eka Hospital",
                sebabKematian = "Leukemia",
                noSurat = "SKM-92839",
                alamat = "Jl. Soekarno Hatta No. 120",
                imageUrl = "https://randomuser.me/api/portraits/women/52.jpg"
            ),
            KematianEntity(
                nama = "Roni Hidayat",
                nik = "147101080479001",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "11/08/2026",
                lokasi = "Rumah Kediaman",
                sebabKematian = "Serangan Jantung",
                noSurat = "SKM-92840",
                alamat = "Jl. Durian Gg. Rambutan",
                imageUrl = "https://randomuser.me/api/portraits/men/53.jpg"
            ),
            KematianEntity(
                nama = "Endang Sri",
                nik = "147106691055002",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "20/08/2026",
                lokasi = "RS Hermina",
                sebabKematian = "Komplikasi Ginjal",
                noSurat = "SKM-92841",
                alamat = "Jl. Tuanku Tambusai Ujung",
                imageUrl = "https://randomuser.me/api/portraits/women/54.jpg"
            ),
            KematianEntity(
                nama = "Agus Zalukhu",
                nik = "147104190874004",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "01/09/2026",
                lokasi = "Rumah Kediaman",
                sebabKematian = "TBC",
                noSurat = "SKM-92842",
                alamat = "Jl. Sembilang No. 34",
                imageUrl = "https://randomuser.me/api/portraits/men/55.jpg"
            ),
            KematianEntity(
                nama = "Putri Ayu",
                nik = "147107520101001",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "10/09/2026",
                lokasi = "RSUD Arifin Achmad",
                sebabKematian = "Demam Tinggi",
                noSurat = "SKM-92843",
                alamat = "Jl. KH. Ahmad Dahlan No. 8",
                imageUrl = "https://randomuser.me/api/portraits/women/56.jpg"
            ),
            KematianEntity(
                nama = "Zulkifli",
                nik = "147102040658002",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "22/09/2026",
                lokasi = "RS Tabrani",
                sebabKematian = "Penyakit Paru Obstruktif",
                noSurat = "SKM-92844",
                alamat = "Jl. Imam Munandar No. 88",
                imageUrl = "https://randomuser.me/api/portraits/men/57.jpg"
            ),
            KematianEntity(
                nama = "Rusmini",
                nik = "147108441149001",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "04/10/2026",
                lokasi = "Rumah Kediaman",
                sebabKematian = "Sakit Tua",
                noSurat = "SKM-92845",
                alamat = "Jl. Pepaya Gg. Nangka",
                imageUrl = "https://randomuser.me/api/portraits/women/58.jpg"
            ),
            KematianEntity(
                nama = "Yusuf Mansur",
                nik = "147103290380002",
                jenisKelamin = "Laki-laki",
                tanggalMeninggal = "15/10/2026",
                lokasi = "RSUD Arifin Achmad",
                sebabKematian = "Penyakit Liver",
                noSurat = "SKM-92846",
                alamat = "Jl. Dahlia No. 54",
                imageUrl = "https://randomuser.me/api/portraits/men/59.jpg"
            ),
            KematianEntity(
                nama = "Santi Rahayu",
                nik = "147106701294003",
                jenisKelamin = "Perempuan",
                tanggalMeninggal = "27/10/2026",
                lokasi = "Rumah Kediaman",
                sebabKematian = "Kanker Rahim",
                noSurat = "SKM-92847",
                alamat = "Jl. Cempaka No. 19",
                imageUrl = "https://randomuser.me/api/portraits/women/60.jpg"
            )
        )
    }

    private fun showDetailDialog(entity: KematianEntity) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_detail_peristiwa, null)
        val tvTitle = dialogView.findViewById<android.widget.TextView>(R.id.tvTitle)
        val tvDetails = dialogView.findViewById<android.widget.TextView>(R.id.tvDetails)
        val imgAttachment = dialogView.findViewById<android.widget.ImageView>(R.id.imgAttachment)

        tvTitle.text = "Detail Peristiwa Kematian"
        tvDetails.text = 
            "Nama           : ${entity.nama}\n" +
            "NIK            : ${entity.nik}\n" +
            "No. Surat      : ${entity.noSurat}\n" +
            "Tgl Meninggal  : ${entity.tanggalMeninggal}\n" +
            "Lokasi         : ${entity.lokasi}\n" +
            "Sebab Kematian : ${entity.sebabKematian}\n" +
            "Jenis Kelamin  : ${entity.jenisKelamin}\n" +
            "Alamat         : ${entity.alamat}\n" +
            "Status         : ${entity.status}"

        com.bumptech.glide.Glide.with(requireContext())
            .load(entity.imageUrl)
            .placeholder(R.drawable.ic_document)
            .error(R.drawable.ic_document)
            .into(imgAttachment)

        com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Tutup") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}