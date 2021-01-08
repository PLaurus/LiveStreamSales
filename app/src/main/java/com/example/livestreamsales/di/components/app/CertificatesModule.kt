package com.example.livestreamsales.di.components.app

import android.content.Context
import android.util.Log
import com.example.livestreamsales.R
import dagger.Module
import dagger.Provides
import okhttp3.tls.HandshakeCertificates
import java.io.BufferedInputStream
import java.io.IOException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.inject.Named
import javax.net.ssl.HostnameVerifier

@Module
class CertificatesModule {
    companion object{
        private val logTag = this::class.simpleName
        private const val DEPENDENCY_NAME_RAW_CERTIFICATE_IDS = "RAW_CERTIFICATE_IDS"
        private const val DEPENDENCY_NAME_X509_CERTIFICATE_FACTORY = "X509_CERTIFICATE_FACTORY"
    }

    @Provides
    fun provideHandshakeCertificates(certificates: Set<@JvmSuppressWildcards X509Certificate>): HandshakeCertificates {
        val builder = HandshakeCertificates.Builder()

        certificates.forEach {
            builder.addTrustedCertificate(it)
        }

        return builder
            .addPlatformTrustedCertificates()
            .build()
    }

    @Provides
    fun provideCertificatesSet(
        context: Context,
        @Named(DEPENDENCY_NAME_RAW_CERTIFICATE_IDS)
        rawCertificateIds: Set<Int>,
        @Named(DEPENDENCY_NAME_X509_CERTIFICATE_FACTORY)
        x509CertificateFactory: CertificateFactory
    ): Set<X509Certificate>{
        val certificates = mutableSetOf<X509Certificate>()
        rawCertificateIds.forEachIndexed { index, certificateId ->
            var bufferedInputStream: BufferedInputStream? = null

            try{
                bufferedInputStream = BufferedInputStream(
                    context.resources.openRawResource(certificateId)
                )

                (x509CertificateFactory.generateCertificate(bufferedInputStream) as? X509Certificate)?.let { certificate ->
                    certificates.add(certificate)
                } ?: Log.e(logTag, "Invalid certificate with index $index")
            } catch (ioException: IOException) {
                Log.e(logTag, "Error occurred while operating certificate file", ioException)
            } catch (certificateException: CertificateException) {
                Log.e(logTag, "Error occurred while generating certificate", certificateException)
            } finally {
                bufferedInputStream?.close()
            }
        }

        return certificates
    }

    @Provides
    @Named(DEPENDENCY_NAME_RAW_CERTIFICATE_IDS)
    fun provideRawCertificateIdsSet(): Set<Int>{
        return setOf(
            R.raw.certificate
        )
    }

    @Provides
    @Named(DEPENDENCY_NAME_X509_CERTIFICATE_FACTORY)
    fun provideX509CertificateFactory(): CertificateFactory {
        return CertificateFactory.getInstance("X.509")
    }

    @Provides
    fun provideHostNameVerifier(): HostnameVerifier{
        return HostnameVerifier { _, _ -> true }
    }
}