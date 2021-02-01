package tv.wfc.livestreamsales.di.components.app.modules.certificates

import android.content.Context
import tv.wfc.livestreamsales.BuildConfig
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.errors.IApplicationErrorsLogger
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
import javax.net.ssl.HttpsURLConnection

@Module
class CertificatesModule {
    companion object{
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
        x509CertificateFactory: CertificateFactory,
        applicationErrorsLogger: IApplicationErrorsLogger
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
                } ?: applicationErrorsLogger.logError(Exception("Invalid certificate with index $index"))
            } catch (ioException: IOException) {
                applicationErrorsLogger.logError(ioException)
            } catch (certificateException: CertificateException) {
                applicationErrorsLogger.logError(certificateException)
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
        if(BuildConfig.DEBUG && BuildConfig.IgnoreRestHostNameVerification){
            return HostnameVerifier { _, _ -> true }
        }

        return HttpsURLConnection.getDefaultHostnameVerifier()
    }
}